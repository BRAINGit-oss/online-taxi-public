package com.brain.apiboss.controller;

import com.brain.apiboss.service.BossDriverCarBindingRelationshipService;
import com.brain.servicepassengeruser.internalcommon.dto.DriverCarBindingRelationship;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DriverCarBindingRelationshipController {

    @Autowired
    BossDriverCarBindingRelationshipService driverCarBindingRelationshipService;

    @PostMapping("/bind")
    ResponseResult bind(@RequestBody DriverCarBindingRelationship driverCarBindingRelationship){

        return driverCarBindingRelationshipService.bindService(driverCarBindingRelationship);
    }

    @PostMapping("/unbind")
    ResponseResult unbind(@RequestBody DriverCarBindingRelationship driverCarBindingRelationship){

        return driverCarBindingRelationshipService.unbindService(driverCarBindingRelationship);
    }


}
