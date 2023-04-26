package com.brain.servicedriveruser.controller;

import com.brain.servicedriveruser.service.DriverUserService;
import com.brain.servicepassengeruser.internalcommon.dto.DriverUser;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DriverUserController {

    @Autowired
    DriverUserService driverUserService;

    @PostMapping("/users")
    public ResponseResult addDriverUser(@RequestBody DriverUser driverUser){
        return driverUserService.addUser(driverUser);
    }

    @PutMapping("/user")
    public ResponseResult updateDriverUser(@RequestBody DriverUser driverUser){
        return driverUserService.updateUser(driverUser);
    }
}
