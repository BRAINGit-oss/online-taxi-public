package com.brain.serviceorder.remote;

import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-driver-user")
public interface ServiceCityDriverUserClient {

    @RequestMapping(method = RequestMethod.GET,value = "/city-driver/is_available_driver")
    public ResponseResult<Boolean> isAvailableDriver(@RequestParam String cityCode);
}
