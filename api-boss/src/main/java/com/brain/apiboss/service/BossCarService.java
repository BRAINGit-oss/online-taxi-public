package com.brain.apiboss.service;

import com.brain.apiboss.remote.DriverUserClient;
import com.brain.servicepassengeruser.internalcommon.dto.Car;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BossCarService {

    @Autowired
    private DriverUserClient driverUserClient;

    public ResponseResult addCarService(Car car){
        return driverUserClient.addCarClient(car);
    }
}
