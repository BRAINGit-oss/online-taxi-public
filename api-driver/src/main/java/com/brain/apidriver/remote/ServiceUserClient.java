package com.brain.apidriver.remote;

import com.brain.servicepassengeruser.internalcommon.dto.Car;
import com.brain.servicepassengeruser.internalcommon.dto.DriverUser;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.ApiDriverRequest;
import com.brain.servicepassengeruser.internalcommon.response.CheckPhoneResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient("service-driver-user")
public interface ServiceUserClient {

    @RequestMapping(method = RequestMethod.PUT,value = "/user")
    ResponseResult updateUserClient(@RequestBody DriverUser driverUser);

    @RequestMapping(method = RequestMethod.GET,value = "/check-driver-phone/{driverPhone}")
    ResponseResult<CheckPhoneResponse> checkPhone(@PathVariable("driverPhone") String driverPhone);

    @RequestMapping(method = RequestMethod.GET,value = "/carId")
    public ResponseResult<Car> getCarById(@RequestBody ApiDriverRequest apiDriverRequest);
}
