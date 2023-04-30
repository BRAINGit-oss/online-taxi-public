package com.brain.servicemap.service;

import com.brain.servicemap.remote.MapPointClient;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.PointsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    @Autowired
    MapPointClient mapPointClient;
    public ResponseResult pointUpload(PointsRequest pointsRequest){
        return mapPointClient.pointUpload(pointsRequest);
    }
}
