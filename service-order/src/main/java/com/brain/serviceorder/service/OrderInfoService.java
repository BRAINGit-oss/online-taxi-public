package com.brain.serviceorder.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.brain.serviceorder.mapper.OrderInfoMapper;
import com.brain.serviceorder.remote.MapServicePriceClient;
import com.brain.servicepassengeruser.internalcommon.constant.CommonStatusEnum;
import com.brain.servicepassengeruser.internalcommon.constant.OrderContrants;
import com.brain.servicepassengeruser.internalcommon.dto.OrderInfo;
import com.brain.servicepassengeruser.internalcommon.dto.PriceRule;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.OrderRequest;
import com.brain.servicepassengeruser.internalcommon.util.RedisPrefixUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    OrderInfoMapper orderInfoMapperl;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    public ResponseResult add(OrderRequest orderRequest){

        //判断是否为最新的fare_version
//        ResponseResult<Boolean> latast = mapServicePriceClient.isLatast(orderRequest.getFareType(), orderRequest.getFareVersion());
//        if(!(latast.getData())){
//            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_CHANGED.getCode(),CommonStatusEnum.PRICE_RULE_CHANGED.getValue());
//        }

        //判断是否为黑名单，注意redis的原子操作
//        if (isBlockDevice(orderRequest)) {
//            return ResponseResult.fail(CommonStatusEnum.DEVICE_REQUEST_ERROR.getCode(), CommonStatusEnum.DEVICE_REQUEST_ERROR.getValue());
//        }

        // service-order调用service-price实现城市和计价规则查询
        if(!(isPriceRuleExists(orderRequest))){
            return ResponseResult.fail(CommonStatusEnum.CITY_SERVICE_NOT_EXITS.getCode(),CommonStatusEnum.CITY_SERVICE_NOT_EXITS.getValue());
        }

        //判断有正在进行的订单不允许下单
//        if(isOrderGoingOn(orderRequest.getPassengerId())>0){
//            return ResponseResult.fail(CommonStatusEnum.ORDER_GOING_ON.getCode(),CommonStatusEnum.ORDER_GOING_ON.getValue());
//        }


        //创建订单
//        log.info(orderRequest.getAddress());
//        OrderInfo orderInfo = new OrderInfo();
//        //一次性复制请求信息
//        BeanUtils.copyProperties(orderRequest,orderInfo);
//        orderInfo.setOrderStatus(OrderContrants.ORDER_START);
//        LocalDateTime now = LocalDateTime.now();
//        orderInfo.setGmtCreate(now);
//        orderInfo.setGmtModified(now);
//
//        orderInfoMapperl.insert(orderInfo);


        return ResponseResult.success("");
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
