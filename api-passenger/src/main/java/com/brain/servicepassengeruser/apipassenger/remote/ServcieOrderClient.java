package com.brain.servicepassengeruser.apipassenger.remote;

import com.brain.servicepassengeruser.internalcommon.dto.OrderInfo;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.OrderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("service-order")
public interface ServcieOrderClient {

    @RequestMapping(method = RequestMethod.POST,value="/order-info/add")
    public ResponseResult add(@RequestBody OrderRequest orderRequest);
}
