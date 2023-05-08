package com.brain.serviceorder.remote;

import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.response.DriverWorkStatusResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-driver-user")
public interface ServiceDriverUserClient {

    @RequestMapping(method = RequestMethod.GET,value = "/city-driver/is_available_driver")
    public ResponseResult<Boolean> isAvailableDriver(@RequestParam String cityCode);

    @RequestMapping(method = RequestMethod.GET,value = "/get-available-driver/{carId}")
    public ResponseResult<DriverWorkStatusResponse> getAvailableDriverByCarId(@PathVariable("carId") Long carId);
}
