package com.brain.servicemap.service;

import com.brain.servicemap.remote.MapTerminalClinet;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.response.TerminalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalService {

    /**
     * 调用MapTerminalClient,根据TerminalResponse对象获取tid并返回
     */
    @Autowired
    MapTerminalClinet mapTerminalClinet;

    public ResponseResult<TerminalResponse> addTerminal(String terminalName){
        return mapTerminalClinet.addTerminal(terminalName);
    }
}
