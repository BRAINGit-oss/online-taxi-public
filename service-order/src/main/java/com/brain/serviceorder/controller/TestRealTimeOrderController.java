package com.brain.serviceorder.controller;

import com.brain.serviceorder.mapper.OrderInfoMapper;
import com.brain.serviceorder.remote.ServiceMapClient;
import com.brain.serviceorder.service.OrderInfoService;
import com.brain.servicepassengeruser.internalcommon.dto.OrderInfo;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestRealTimeOrderController {

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    OrderInfoService orderInfoService;

    @Value("${server.port}")
    String port;

    @GetMapping("/test-real-time-order/{orderId}")
    public String dispatchRealTimeOrder(@PathVariable("orderId") Long orderId){
        System.out.println("service-order 端口："+ port+" 并发测试：orderId："+orderId);
        OrderInfo orderInfo1 = orderInfoMapper.selectById(orderId);
        orderInfoService.aroundSearch(orderInfo1);
        return "test-real-time-order   success";
    }
}
