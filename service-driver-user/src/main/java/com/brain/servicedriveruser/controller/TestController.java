package com.brain.servicedriveruser.controller;

import com.brain.servicedriveruser.mapper.DriverUserMapper;
import com.brain.servicedriveruser.service.DriverUserService;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    DriverUserMapper driverUserMapper;

    @Autowired
    DriverUserService driverUserService;

    @GetMapping("/test")
    public String test(){
        return "service-driver-user";
    }

    @GetMapping("/test-db")
    public ResponseResult testdb(){
        return driverUserService.testUser();
    }

    @GetMapping("/test-m")
    public int testm(String cityCode){
        int i = driverUserMapper.selectDriverUserCountByCityCode(cityCode);
        return i;
    }
}
