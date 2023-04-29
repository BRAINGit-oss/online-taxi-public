package com.brain.servicemap.service;

import com.brain.servicemap.remote.MapTrackClient;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.response.TrackResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrackService {

    @Autowired
    MapTrackClient mapTrackClient;

    public ResponseResult<TrackResponse> addTrack(String tid){

        return mapTrackClient.addTrack(tid);
    }
}
