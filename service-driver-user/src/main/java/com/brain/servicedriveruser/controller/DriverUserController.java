package com.brain.servicedriveruser.controller;

import com.brain.servicedriveruser.service.DriverUserService;
import com.brain.servicepassengeruser.internalcommon.constant.DriverCarConstants;
import com.brain.servicepassengeruser.internalcommon.dto.DriverUser;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.response.CheckPhoneResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Driver;

@RestController
public class DriverUserController {

    @Autowired
    DriverUserService driverUserService;

    @PostMapping("/user")
    public ResponseResult addDriverUser(@RequestBody DriverUser driverUser){
        return driverUserService.addUser(driverUser);
    }

    @PutMapping("/user")
    public ResponseResult updateDriverUser(@RequestBody DriverUser driverUser){
        return driverUserService.updateUser(driverUser);
    }

    @GetMapping("/check-driver-phone/{driverPhone}")
    public ResponseResult checkPhone(@PathVariable("driverPhone") String driverPhone){

        CheckPhoneResponse checkPhoneResponse = new CheckPhoneResponse();

        ResponseResult<DriverUser> driverUserByPhone = driverUserService.getDriverUserByPhone(driverPhone);
        DriverUser data = driverUserByPhone.getData();
        if(data == null){
            checkPhoneResponse.setDriverPhone(driverPhone);
            checkPhoneResponse.setIfEmpty(DriverCarConstants.DRIVER_STATUS_NO);
        }else{
            checkPhoneResponse.setDriverPhone(data.getDriverPhone());
            checkPhoneResponse.setIfEmpty(DriverCarConstants.DRIVER_STATUS_YES);
        }

        return ResponseResult.success(checkPhoneResponse);
    }
}
