package com.brain.servicedriveruser.controller;


import com.brain.servicedriveruser.service.IDriverCarBindingRelationshipService;
import com.brain.servicepassengeruser.internalcommon.dto.DriverCarBindingRelationship;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Brain
 * @since 2023-04-26
 */
@RestController
@RequestMapping("/driver_car_binding_relationship")
public class DriverCarBindingRelationshipController {

    @Autowired
    IDriverCarBindingRelationshipService iDriverCarBindingRelationshipService;

    @PostMapping("/bind")
    ResponseResult bind(@RequestBody DriverCarBindingRelationship driverCarBindingRelationship){
        return iDriverCarBindingRelationshipService.bindService(driverCarBindingRelationship);
    }

    @PostMapping("/unbind")
    ResponseResult unbind(@RequestBody DriverCarBindingRelationship driverCarBindingRelationship){
        return iDriverCarBindingRelationshipService.unBindService(driverCarBindingRelationship);
    }

}
