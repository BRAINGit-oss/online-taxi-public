package com.brain.servicedriveruser.service;

import com.brain.servicedriveruser.remote.ServiceMapClient;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.PointsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    @Autowired
    ServiceMapClient serviceMapClient;

    public ResponseResult pointUpload(PointsRequest pointsRequest){

        return serviceMapClient.pointsUpload(pointsRequest);
    }
}
