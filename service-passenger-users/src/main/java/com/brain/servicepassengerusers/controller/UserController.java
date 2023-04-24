package com.brain.servicepassengerusers.controller;

import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.VerificationCodeDTO;
import com.brain.servicepassengerusers.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping("/user")
    public ResponseResult loginOrRegistor(@RequestBody VerificationCodeDTO verificationCodeDTO){

        String passengerPhone = verificationCodeDTO.getPassengerPhone();
        System.out.println("手机号："+passengerPhone);
        return userService.loginOrRegistor(passengerPhone);
    }

    @GetMapping("/user/{phone}")
    public ResponseResult queryByPassengerPhone(@PathVariable("phone") String passengerPhone){
        System.out.println("user service被调用，手机号："+passengerPhone);
        return userService.getUserInfoByPhone(passengerPhone);
    }

}
