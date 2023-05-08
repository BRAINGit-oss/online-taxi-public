package com.brain.apiboss.controller;

import com.brain.apiboss.service.DriverUserService;
import com.brain.servicepassengeruser.internalcommon.dto.DriverUser;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver-user")
public class DriverUserController {

    @Autowired
    DriverUserService driverUserService;

    @PostMapping("/add")
    public ResponseResult addDriverUser(@RequestBody DriverUser driverUser){
        return driverUserService.addUser(driverUser);
    }

    @PutMapping("/update")
    public ResponseResult updateUser(@RequestBody DriverUser driverUser){
        return driverUserService.updateUser(driverUser);
    }
}
