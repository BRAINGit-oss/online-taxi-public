package com.brain.apidriver.service;

import com.brain.apidriver.remote.ServiceUserClient;
import com.brain.servicepassengeruser.internalcommon.dto.DriverUser;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    ServiceUserClient serviceUserClient;

    public ResponseResult updateUser(DriverUser driverUser){
        return serviceUserClient.updateUserClient(driverUser);
    }

}
