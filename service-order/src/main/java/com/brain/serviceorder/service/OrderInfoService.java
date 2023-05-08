package com.brain.serviceorder.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.brain.serviceorder.mapper.OrderInfoMapper;
import com.brain.serviceorder.remote.MapServicePriceClient;
import com.brain.serviceorder.remote.ServiceDriverUserClient;
import com.brain.serviceorder.remote.ServiceMapClient;
import com.brain.servicepassengeruser.internalcommon.constant.CommonStatusEnum;
import com.brain.servicepassengeruser.internalcommon.constant.OrderContrants;
import com.brain.servicepassengeruser.internalcommon.dto.OrderInfo;
import com.brain.servicepassengeruser.internalcommon.dto.PriceRule;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.OrderRequest;
import com.brain.servicepassengeruser.internalcommon.request.PriceRuleIsNewRequest;
import com.brain.servicepassengeruser.internalcommon.response.DriverWorkStatusResponse;
import com.brain.servicepassengeruser.internalcommon.response.TerminalResponse;
import com.brain.servicepassengeruser.internalcommon.util.RedisPrefixUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    ServiceDriverUserClient serviceDriverUserClient;
    @Autowired
    ServiceMapClient serviceMapClient;
    @Autowired
    OrderInfoMapper orderInfoMapperl;
    @Autowired
    RedissonClient redissonClient;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    public ResponseResult add(OrderRequest orderRequest){

        //判断有正在进行的订单不允许下单
        if(isPassengerOrderGoingOn(orderRequest.getPassengerId())>0){
            return ResponseResult.fail(CommonStatusEnum.ORDER_GOING_ON.getCode(),CommonStatusEnum.ORDER_GOING_ON.getValue());
        }

        //根据城市ID查询该城市是否有可用司机
        ResponseResult<Boolean> availableDriver = serviceDriverUserClient.isAvailableDriver(orderRequest.getAddress());
        if(!availableDriver.getData()){
            return ResponseResult.fail(CommonStatusEnum.CITY_DRIVER_EMPTY.getCode(),CommonStatusEnum.CITY_DRIVER_EMPTY.getValue());
        }

        //判断是否为最新的fare_version（计价规则）
        PriceRuleIsNewRequest priceRuleIsNewRequest = new PriceRuleIsNewRequest();
        priceRuleIsNewRequest.setFareType(orderRequest.getFareType());
        priceRuleIsNewRequest.setFareVersion(orderRequest.getFareVersion());
        ResponseResult<Boolean> latast = mapServicePriceClient.isLatast(priceRuleIsNewRequest);
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

        LocalDateTime now = LocalDateTime.now();

        orderInfo.setOrderStatus(OrderContrants.ORDER_START);
        orderInfo.setGmtCreate(now);
        orderInfo.setGmtModified(now);

        orderInfoMapperl.insert(orderInfo);

        //派单业务 调用终端搜索服务
        aroundSearch(orderInfo);

        return ResponseResult.success("");
    }

//    synchronized是重量级的互斥锁 通过字符串定位到局部代码更合适
    public void aroundSearch(OrderInfo orderInfo) {
        String depLongitude = orderInfo.getDepLongitude(); //经度
        String depLatitude = orderInfo.getDepLatitude();  //纬度
        String center = depLatitude+","+depLongitude;

        List<Integer> rediusList = new ArrayList<>();
        rediusList.add(2000);
        rediusList.add(4000);
        rediusList.add(5000);
        ResponseResult<List<TerminalResponse>> listResponseResult =null;

        //解析终端
        //goto语句是为了测试
        redius:
        for(int i=0;i<rediusList.size();i++){

            Integer redius = rediusList.get(i);
            listResponseResult = serviceMapClient.aroundSearch(center, redius);
            log.info("查询半径为"+redius+"范围内的车辆"+ JSONArray.fromObject(listResponseResult.getData()).toString());
            //获得终端 [{"carId":234152342,"tid":"679730762",经度,纬度},{"carId":1652578903615488002,"tid":"682441028"}]


            //解析终端
            List<TerminalResponse> result = listResponseResult.getData();

            for(int j=0;j< result.size();j++){
                TerminalResponse terminalResponse = result.get(j);
                Long carId = terminalResponse.getCarId();
                //车辆经纬度从地图中获取
                String latitude = terminalResponse.getLatitude();
                String longitude = terminalResponse.getLongitude();

                //查询是否有对应的可派单司机 DRIVER_WORK_EMPTY CAR_WORK_EMPTY
                ResponseResult<DriverWorkStatusResponse> availableDriverByCarId = serviceDriverUserClient.getAvailableDriverByCarId(carId);
//                availableDriverByCarId.getCode()==CommonStatusEnum.CAR_WORK_EMPTY.getCode()
                if(availableDriverByCarId.getCode()==CommonStatusEnum.DRIVER_WORK_EMPTY.getCode()){
                    log.info("没有车辆ID："+carId+",对应的司机");
                    continue;
                }else{
                    log.info("车辆ID："+carId+"找到了正在出车的司机");
                    DriverWorkStatusResponse data = availableDriverByCarId.getData();
                    Long driverId = data.getDriverId();
                    String driverPhone = data.getDriverPhone();
                    String licenseId = data.getLicenseId();
                    String vehicleNo = data.getVehicleNo();

                    String locKey = (driverId + "").intern();
                    RLock lock = redissonClient.getLock(locKey);
                    lock.lock();
                    log.info("locKey is: "+locKey);

                    //定位到核心代码 连接池中取字符串
                    //判断有正在进行的订单不允许下单
                    if(isDriverOrderGoingOn(driverId)>0){
                        lock.unlock();
                        continue ;
                    }
                    //完善订单信息
                    QueryWrapper<OrderInfo> objectQueryWrapper = new QueryWrapper<>();
                    objectQueryWrapper.eq("id",carId);

                    orderInfo.setDriverId(driverId);
                    orderInfo.setDriverPhone(driverPhone);
                    orderInfo.setCarId(carId);

                    orderInfo.setReceiveOrderTime(LocalDate.now());
                    orderInfo.setReceiveOrderCarLongitude(longitude);
                    orderInfo.setReceiveOrderCarLatitude(latitude);

                    //司机信息，车辆信息中获取
                    orderInfo.setLicenseId(licenseId);
                    orderInfo.setVehicleNo(vehicleNo);
                    orderInfo.setOrderStatus(OrderContrants.RECEIVE_ORDER);

                    orderInfoMapperl.updateById(orderInfo);

                    lock.unlock();
                    //如果派单成功，则退出循环
                    break redius;
                }

            }

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

    public int isPassengerOrderGoingOn(Long passengerId){

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

    public int isDriverOrderGoingOn(Long driverId){

        QueryWrapper<OrderInfo> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("driver_id",driverId);
        objectQueryWrapper.and(objectQueryWrapper1 -> objectQueryWrapper1.eq("order_status",OrderContrants.RECEIVE_ORDER)
                .or().eq("order_status",OrderContrants.DRIVER_TO_PICK_UP_PASSENGER)
                .or().eq("order_status",OrderContrants.DRIVER_ADDRIVED_DEPATURE)
                .or().eq("order_status",OrderContrants.PICK_UP_PASSENGER)
        );
        Integer orderGoingOnNumber = orderInfoMapperl.selectCount(objectQueryWrapper);
        log.info("司机"+driverId+"正在进行的订单数量为："+orderGoingOnNumber);

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
