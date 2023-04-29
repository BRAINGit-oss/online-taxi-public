package com.brain.servicemap.controller;

import com.brain.servicemap.service.TrackService;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.response.TrackResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trace")
public class TrackController {

    @Autowired
    TrackService trackService;

    @PostMapping("/add")
    public ResponseResult<TrackResponse> addTrack(String tid){

        return trackService.addTrack(tid);
    }
}
