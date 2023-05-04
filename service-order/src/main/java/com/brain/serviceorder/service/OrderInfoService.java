package com.brain.serviceorder.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.brain.serviceorder.mapper.OrderInfoMapper;
import com.brain.serviceorder.remote.MapServicePriceClient;
import com.brain.serviceorder.remote.ServiceCityDriverUserClient;
import com.brain.serviceorder.remote.ServiceMapClient;
import com.brain.servicepassengeruser.internalcommon.constant.CommonStatusEnum;
import com.brain.servicepassengeruser.internalcommon.constant.OrderContrants;
import com.brain.servicepassengeruser.internalcommon.dto.OrderInfo;
import com.brain.servicepassengeruser.internalcommon.dto.PriceRule;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.OrderRequest;
import com.brain.servicepassengeruser.internalcommon.response.TerminalResponse;
import com.brain.servicepassengeruser.internalcommon.util.RedisPrefixUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Brain
 * @since 2023-05-01
 */
@Service
@Slf4j
public class OrderInfoService {

    @Autowired
    MapServicePriceClient mapServicePriceClient;
    @Autowired
    ServiceCityDriverUserClient serviceCityDriverUserClient;
    @Autowired
    ServiceMapClient serviceMapClient;
    @Autowired
    OrderInfoMapper orderInfoMapperl;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    public ResponseResult add(OrderRequest orderRequest){

        //判断有正在进行的订单不允许下单
        if(isOrderGoingOn(orderRequest.getPassengerId())>0){
            return ResponseResult.fail(CommonStatusEnum.ORDER_GOING_ON.getCode(),CommonStatusEnum.ORDER_GOING_ON.getValue());
        }

        //根据城市ID查询该城市是否有可用司机
        ResponseResult<Boolean> availableDriver = serviceCityDriverUserClient.isAvailableDriver(orderRequest.getAddress());
        if(!availableDriver.getData()){
            return ResponseResult.fail(CommonStatusEnum.CITY_DRIVER_EMPTY.getCode(),CommonStatusEnum.CITY_DRIVER_EMPTY.getValue());
        }

        //判断是否为最新的fare_version
        ResponseResult<Boolean> latast = mapServicePriceClient.isLatast(orderRequest.getFareType(), orderRequest.getFareVersion());
        if(!(latast.getData())){
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_CHANGED.getCode(),CommonStatusEnum.PRICE_RULE_CHANGED.getValue());
        }

        //判断是否为黑名单，注意redis的原子操作
        if (isBlockDevice(orderRequest)) {
            return ResponseResult.fail(CommonStatusEnum.DEVICE_REQUEST_ERROR.getCode(), CommonStatusEnum.DEVICE_REQUEST_ERROR.getValue());
        }

        // service-order调用service-price实现城市和计价规则查询
        if(!(isPriceRuleExists(orderRequest))){
            return ResponseResult.fail(CommonStatusEnum.CITY_SERVICE_NOT_EXITS.getCode(),CommonStatusEnum.CITY_SERVICE_NOT_EXITS.getValue());
        }

        //创建订单
        log.info(orderRequest.getAddress());
        OrderInfo orderInfo = new OrderInfo();
        //一次性复制请求信息
        BeanUtils.copyProperties(orderRequest,orderInfo);
        orderInfo.setOrderStatus(OrderContrants.ORDER_START);
        LocalDateTime now = LocalDateTime.now();
        orderInfo.setGmtCreate(now);
        orderInfo.setGmtModified(now);

        orderInfoMapperl.insert(orderInfo);


        //派单业务 调用终端搜索服务
        aroundSearch(orderRequest);

        return ResponseResult.success("");
    }

    private void aroundSearch(OrderRequest orderRequest) {
        String depLongitude = orderRequest.getDepLongitude(); //经度
        String depLatitude = orderRequest.getDepLatitude();  //纬度
        String center = depLatitude+","+depLongitude;

        List<Integer> rediusList = new ArrayList<>();
        rediusList.add(2000);
        rediusList.add(4000);
        rediusList.add(5000);
        ResponseResult<List<TerminalResponse>> listResponseResult =null;
        for(int i=0;i<rediusList.size();i++){
            Integer redius = rediusList.get(i);
            listResponseResult = serviceMapClient.aroundSearch(center, redius);
            log.info("查询半径为"+redius+"范围内的车辆"+ JSONArray.fromObject(listResponseResult.getData()).toString());
            //获得终端 [{"carId":234152342,"tid":"679730762"},{"carId":1652578903615488002,"tid":"682441028"}]
            TerminalResponse terminalResponse = listResponseResult.getData().get(i);
            //解析终端
            Long carId = terminalResponse.getCarId();
            //根据解析出的车辆，查询车辆信息(1、出车状态的司机service-driver-user做 2、司机没有正在进行的订单service-order)



            //找到符合的车辆，进行派单

            //如果派单成功，则退出循环

        }
    }

    private boolean isBlockDevice(OrderRequest orderRequest) {
        String keyByDevice = RedisPrefixUtils.generatorKeyByDevice(orderRequest.getDeviceCode());
        Boolean aBoolean = stringRedisTemplate.hasKey(keyByDevice);
        if(aBoolean){
            String s = stringRedisTemplate.opsForValue().get(keyByDevice);
            Integer value = Integer.parseInt(s);
            if(value >=2){
                return true;
            }else{
                stringRedisTemplate.opsForValue().increment(keyByDevice);
            }
        }else{
            stringRedisTemplate.opsForValue().setIfAbsent(keyByDevice,"1",1L, TimeUnit.HOURS);
        }
        return false;
    }

    public int isOrderGoingOn(Long passengerId){

        QueryWrapper<OrderInfo> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("passenger_id",passengerId);
        objectQueryWrapper.and(objectQueryWrapper1 -> objectQueryWrapper1.eq("order_status",OrderContrants.ORDER_START)
                .or().eq("order_status",OrderContrants.RECEIVE_ORDER)
                .or().eq("order_status",OrderContrants.DRIVER_TO_PICK_UP_PASSENGER)
                .or().eq("order_status",OrderContrants.DRIVER_ADDRIVED_DEPATURE)
                .or().eq("order_status",OrderContrants.PICK_UP_PASSENGER)
                .or().eq("order_status",OrderContrants.PASSENGER_GETOFF)
                .or().eq("order_status",OrderContrants.TO_START_PAY)
        );
        Integer orderGoingOnNumber = orderInfoMapperl.selectCount(objectQueryWrapper);

        return orderGoingOnNumber;
    }

    public boolean isPriceRuleExists(OrderRequest orderRequest){
        String fareType = orderRequest.getFareType();
        int index = fareType.indexOf("$");
        String cityCode = fareType.substring(0, index);
        String fareVersion1 = fareType.substring(index + 1);

        PriceRule priceRule1 = new PriceRule();
        priceRule1.setCityCode(cityCode);
        priceRule1.setVehicleType(fareVersion1);
        ResponseResult<Boolean> result = mapServicePriceClient.ifExits(priceRule1);

        return result.getData();
    }

}
