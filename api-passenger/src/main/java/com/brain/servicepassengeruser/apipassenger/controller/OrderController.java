package com.brain.servicepassengeruser.apipassenger.controller;

import com.brain.servicepassengeruser.apipassenger.service.OrderServcie;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.OrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderServcie orderServcie;

    @PostMapping("/add")
    public ResponseResult orderAdd(@RequestBody OrderRequest orderRequest){
        log.info(orderRequest.getAddress());

        return orderServcie.add(orderRequest);
    }
}
