package com.brain.servicepassengeruser.apipassenger.controller;

import com.brain.servicepassengeruser.apipassenger.remote.ServcieOrderClient;
import com.brain.servicepassengeruser.internalcommon.dto.OrderInfo;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;

@RestController
public class TestController {

    @Value("${server.port}")
    String port;
    @GetMapping("/test")
    public String test(){
        return "test api passenger";
    }

    @GetMapping("/authTest")
    public ResponseResult test1(){
        return ResponseResult.success("auth test");
    }

    @GetMapping("/noauthTest")
    public ResponseResult test2(){
        return ResponseResult.success("no auth test");
    }

    @Autowired
    ServcieOrderClient servcieOrderClient;
    @GetMapping("/test-real-time-order/{orderId}")
    public String dispatchRealTimeOrder(@PathVariable("orderId") Long orderId){
        System.out.println("service-order 端口："+ port+"并发测试：orderId："+orderId);
        servcieOrderClient.dispatchRealTimeOrder(orderId);
        return "test-real-time-order   success";
    }
}
