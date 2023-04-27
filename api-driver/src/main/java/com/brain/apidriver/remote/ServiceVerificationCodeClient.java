package com.brain.apidriver.remote;

import com.brain.servicepassengeruser.internalcommon.dto.DriverUser;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.response.CheckPhoneResponse;
import com.brain.servicepassengeruser.internalcommon.response.NumberCodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("service-verificationcode")
public interface ServiceVerificationCodeClient {

    @RequestMapping(method = RequestMethod.GET,value = "/numberCode/{size}")
    ResponseResult<NumberCodeResponse> getVerification(@PathVariable("size") int size);
}
