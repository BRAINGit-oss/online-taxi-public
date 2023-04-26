package com.brain.servicedriveruser.service;

import com.brain.servicedriveruser.mapper.DriverUserMapper;
import com.brain.servicepassengeruser.internalcommon.dto.DriverUser;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public ResponseResult addUser(DriverUser driverUser) {
        LocalDateTime now = LocalDateTime.now();
        driverUser.setGmtCreate(now);
        driverUser.setGmtModify(now);

        driverUserMapper.insert(driverUser);
        return ResponseResult.success("");
    }
}
