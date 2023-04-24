package com.brain.servicepassengeruser.apipassenger.remote;

import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.VerificationCodeDTO;
import com.brain.servicepassengeruser.internalcommon.response.NumberCodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("service-passenger-users")
public interface ServicePassengerUsersClient {

    @RequestMapping(method = RequestMethod.POST,value="/user")
    ResponseResult getNumberCode(@RequestBody VerificationCodeDTO verificationCodeDTO);

    @RequestMapping(method=RequestMethod.GET,value="/user/{phone}")
    ResponseResult getUserByPhone(@PathVariable("phone") String passengerPhone);

}
