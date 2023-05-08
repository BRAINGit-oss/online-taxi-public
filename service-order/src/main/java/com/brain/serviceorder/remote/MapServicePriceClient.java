package com.brain.serviceorder.remote;

import com.brain.servicepassengeruser.internalcommon.dto.PriceRule;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.PriceRuleIsNewRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("service-price")
public interface MapServicePriceClient {

    @RequestMapping(method = RequestMethod.POST,value = "/price-rule/is_latest")
    public ResponseResult<Boolean> isLatast(@RequestBody PriceRuleIsNewRequest priceRuleIsNewRequest);

    @RequestMapping(method = RequestMethod.POST,value = "/price-rule/if_exits")
    public ResponseResult<Boolean> ifExits(@RequestBody PriceRule priceRule);
}
