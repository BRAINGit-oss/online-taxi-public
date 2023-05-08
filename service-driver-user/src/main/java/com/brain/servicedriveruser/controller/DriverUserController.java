package com.brain.servicedriveruser.controller;

import com.brain.servicedriveruser.service.DriverUserService;
import com.brain.servicepassengeruser.internalcommon.constant.CommonStatusEnum;
import com.brain.servicepassengeruser.internalcommon.constant.DriverCarConstants;
import com.brain.servicepassengeruser.internalcommon.dto.DriverUser;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.OrderRequest;
import com.brain.servicepassengeruser.internalcommon.response.CheckPhoneResponse;
import com.brain.servicepassengeruser.internalcommon.response.DriverWorkStatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Driver;

@RestController
public class DriverUserController {

    @Autowired
    DriverUserService driverUserService;

    @PostMapping("/add")
    public ResponseResult addDriverUser(@RequestBody DriverUser driverUser){
        return driverUserService.addUser(driverUser);
    }

    @PutMapping("/update")
    public ResponseResult updateDriverUser(@RequestBody DriverUser driverUser){
        return driverUserService.updateUser(driverUser);
    }

    @GetMapping("/check-driver-phone/{driverPhone}")
    public ResponseResult<CheckPhoneResponse> checkPhone(@PathVariable("driverPhone") String driverPhone){

        CheckPhoneResponse checkPhoneResponse = new CheckPhoneResponse();

        ResponseResult<DriverUser> driverUserByPhone = driverUserService.getDriverUserByPhone(driverPhone);
        int ifExists = DriverCarConstants.DRIVER_STATUS_YES;
        if(driverUserByPhone.getCode()== CommonStatusEnum.DRIVER_STATUS_UNVALID.getCode()){
            ifExists = DriverCarConstants.DRIVER_STATUS_NO;
            checkPhoneResponse.setDriverPhone(driverPhone);
            checkPhoneResponse.setIfEmpty(ifExists);
        }else{
            checkPhoneResponse.setDriverPhone(driverUserByPhone.getData().getDriverPhone());
            checkPhoneResponse.setIfEmpty(ifExists);
        }

        return ResponseResult.success(checkPhoneResponse);
    }

    @GetMapping("/get-available-driver/{carId}")
    public ResponseResult<DriverWorkStatusResponse> getAvailableDriverByCarId(@PathVariable Long carId){

        return driverUserService.getAvailableDriverByCarId(carId);
    }
}
