package com.brain.serviceorder.remote;

import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-price")
public interface MapServicePriceClient {

    @RequestMapping(method = RequestMethod.GET,value = "/price-rule/is_latest")
    public ResponseResult<Boolean> isLatast(@RequestParam String fareType, @RequestParam Integer fareVersion);
}
