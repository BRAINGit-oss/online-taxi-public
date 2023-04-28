package com.brain.servicemap.service;

import com.brain.servicemap.remote.MapServiceClient;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.response.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateService {

    @Autowired
    MapServiceClient mapServiceClient;

    /**
     * 调用MapServiceClient
     */
    public ResponseResult<ServiceResponse> add(String nameService){
        return mapServiceClient.addService(nameService);
    }
}
