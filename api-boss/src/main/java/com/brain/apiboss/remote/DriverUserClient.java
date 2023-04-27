package com.brain.apiboss.remote;

import com.brain.servicepassengeruser.internalcommon.dto.Car;
import com.brain.servicepassengeruser.internalcommon.dto.DriverCarBindingRelationship;
import com.brain.servicepassengeruser.internalcommon.dto.DriverUser;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.VerificationCodeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("service-driver-user")
public interface DriverUserClient {

    @RequestMapping(method = RequestMethod.POST,value="/users")
    ResponseResult addUser(@RequestBody DriverUser driverUser);

    @RequestMapping(method = RequestMethod.PUT,value="/user")
    ResponseResult updateUser(@RequestBody DriverUser driverUser);

    @RequestMapping(method = RequestMethod.POST,value="/driver_car_binding_relationship/bind")
    ResponseResult bindClient(@RequestBody DriverCarBindingRelationship driverCarBindingRelationship);

    @RequestMapping(method = RequestMethod.POST,value="/driver_car_binding_relationship/unbind")
    ResponseResult unbindClient(@RequestBody DriverCarBindingRelationship driverCarBindingRelationship);

    @RequestMapping(method = RequestMethod.POST,value="/car")
    ResponseResult addCarClient(@RequestBody Car car);
}
