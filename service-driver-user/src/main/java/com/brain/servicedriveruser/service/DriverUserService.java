package com.brain.servicedriveruser.service;

import com.brain.servicedriveruser.mapper.DriverUserMapper;
import com.brain.servicepassengeruser.internalcommon.dto.DriverUser;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DriverUserService {

    @Autowired
    DriverUserMapper driverUserMapper;

    public ResponseResult testUser(){

        DriverUser driverUser = driverUserMapper.selectById(1);

        return ResponseResult.success(driverUser);
    }
}
