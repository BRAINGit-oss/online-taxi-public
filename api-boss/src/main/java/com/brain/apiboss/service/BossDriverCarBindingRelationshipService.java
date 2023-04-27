package com.brain.apiboss.service;

import com.brain.apiboss.remote.DriverUserClient;
import com.brain.servicepassengeruser.internalcommon.dto.DriverCarBindingRelationship;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BossDriverCarBindingRelationshipService {

    @Autowired
    private DriverUserClient driverUserClient;

    public ResponseResult bindService(DriverCarBindingRelationship driverCarBindingRelationship){
        return driverUserClient.bindClient(driverCarBindingRelationship);
    }

    public ResponseResult unbindService(DriverCarBindingRelationship driverCarBindingRelationship){
        return driverUserClient.unbindClient(driverCarBindingRelationship);
    }
}
