package com.brain.apidriver.remote;

import com.brain.servicepassengeruser.internalcommon.dto.DriverUser;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient("service-driver-user")
public interface ServiceUserClient {

    @RequestMapping(method = RequestMethod.PUT,value = "/user")
    ResponseResult updateUserClient(@RequestBody DriverUser driverUser);

}
