package com.brain.apiboss.service;

import com.brain.apiboss.remote.DriverUserClient;
import com.brain.servicepassengeruser.internalcommon.dto.DriverUser;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.Response;

@Service
public class DriverUserService {

    @Autowired
    private DriverUserClient driverUserClient;

    public ResponseResult addUser(DriverUser driverUser){
        return driverUserClient.addUser(driverUser);
    }

    public ResponseResult updateUser(DriverUser driverUser){
        return driverUserClient.updateUser(driverUser);
    }
}
