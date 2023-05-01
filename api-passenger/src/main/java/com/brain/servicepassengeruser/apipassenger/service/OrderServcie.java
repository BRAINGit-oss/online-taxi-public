package com.brain.servicepassengeruser.apipassenger.service;

import com.brain.servicepassengeruser.apipassenger.remote.ServcieOrderClient;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServcie {

    @Autowired
    ServcieOrderClient servcieOrderClient;

    public ResponseResult add(OrderRequest orderRequest){

        return servcieOrderClient.add(orderRequest);
    }
}
