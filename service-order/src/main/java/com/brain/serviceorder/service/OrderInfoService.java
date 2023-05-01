package com.brain.serviceorder.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.brain.serviceorder.mapper.OrderInfoMapper;
import com.brain.serviceorder.remote.MapServicePriceClient;
import com.brain.servicepassengeruser.internalcommon.constant.CommonStatusEnum;
import com.brain.servicepassengeruser.internalcommon.constant.OrderContrants;
import com.brain.servicepassengeruser.internalcommon.dto.OrderInfo;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.OrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
    public ResponseResult add(OrderRequest orderRequest){

        //判断是否为最新的fare_version
        ResponseResult<Boolean> latast = mapServicePriceClient.isLatast(orderRequest.getFareType(), orderRequest.getFareVersion());
        if(!(latast.getData())){
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_CHANGED.getCode(),CommonStatusEnum.PRICE_RULE_CHANGED.getValue());
        }

        //判断有正在进行的订单不允许下单
        if(isOrderGoingOn(orderRequest.getPassengerId())>0){
            return ResponseResult.fail(CommonStatusEnum.ORDER_GOING_ON.getCode(),CommonStatusEnum.ORDER_GOING_ON.getValue());
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


        return ResponseResult.success("");
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

}
