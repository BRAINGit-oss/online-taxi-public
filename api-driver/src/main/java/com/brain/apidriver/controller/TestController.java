package com.brain.apidriver.controller;

import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/auth")
    String testAuth(){
        return "auth";
    }

    @RequestMapping("/noauth")
    String testNoAuth(){
        return "no auth";
    }
}
