package com.brain.servicedriveruser.remote;


import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.PointsRequest;
import com.brain.servicepassengeruser.internalcommon.response.ServiceResponse;
import com.brain.servicepassengeruser.internalcommon.response.TerminalResponse;
import com.brain.servicepassengeruser.internalcommon.response.TrackResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-map")
public interface TerminalClient {

    @RequestMapping(method = RequestMethod.POST,value="/terminal/add")
    ResponseResult<TerminalResponse> addTerminal(@RequestParam String name);

    @RequestMapping(method = RequestMethod.POST,value = "/trace/add")
    public ResponseResult<TrackResponse> addTrack(@RequestParam  String tid);

    @RequestMapping(method = RequestMethod.POST,value = "/point/upload")
    public ResponseResult pointsUpload(@RequestBody PointsRequest pointsRequest);
}
