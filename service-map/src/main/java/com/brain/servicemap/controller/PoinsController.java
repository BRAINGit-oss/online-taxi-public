package com.brain.servicemap.controller;

import com.brain.servicemap.service.PointService;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.PointsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/point")
public class PoinsController {

    @Autowired
    PointService pointService;

    @PostMapping("/upload")
    public ResponseResult pointUpload(@RequestBody PointsRequest pointsRequest){

        return pointService.pointUpload(pointsRequest);
    }
}
