package com.brain.apipassenger.controller;

import com.brain.apipassenger.request.VerificationCodeDTO;
import com.brain.apipassenger.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerificationCodeController {
   @Autowired
   private VerificationCodeService verificationCodeService;
    @GetMapping("/verification-code")
    public String verificationCode(@RequestBody VerificationCodeDTO verificationCodeDTO){
        String passengerPhone = verificationCodeDTO.getPassengerPhone();
        System.out.println("接受到的手机号为:"+passengerPhone);
        return verificationCodeService.generatorCode(passengerPhone);
    }
}
