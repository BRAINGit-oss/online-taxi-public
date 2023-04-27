package com.brain.apidriver.controller;

import com.brain.apidriver.service.VerificationCodeService;
import com.brain.servicepassengeruser.internalcommon.dto.DriverUser;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.VerificationCodeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class VerificationCodeController {

    @Autowired
    VerificationCodeService verificationCodeService;

    @GetMapping("/verification-code")
    ResponseResult checkUser(@RequestBody DriverUser driverUser){
        String driverPhone = driverUser.getDriverPhone();
        return verificationCodeService.getVerificationCode(driverPhone);
    }

    @PostMapping("/verification-code-check")
    public ResponseResult checkVerificationCode(@RequestBody VerificationCodeDTO verificationCodeDTO){
        String driverPhone = verificationCodeDTO.getDriverPhone();
        String verificationCode = verificationCodeDTO.getVerificationCode();
        System.out.println("手机号："+driverPhone+",验证码："+verificationCode);

        return verificationCodeService.checkCode(driverPhone,verificationCode);
    }
}
