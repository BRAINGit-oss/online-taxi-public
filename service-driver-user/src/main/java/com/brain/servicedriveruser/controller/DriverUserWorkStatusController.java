package com.brain.servicedriveruser.controller;


import com.brain.servicedriveruser.service.IDriverUserWorkStatusService;
import com.brain.servicepassengeruser.internalcommon.dto.DriverUserWorkStatus;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 2023-04-27
 */
@RestController
public class DriverUserWorkStatusController {

    @Autowired
    IDriverUserWorkStatusService iDriverUserWorkStatusService;

    @RequestMapping("/update")
    ResponseResult add(@RequestBody DriverUserWorkStatus driverUserWorkStatus){
        return iDriverUserWorkStatusService.updateWorkState(driverUserWorkStatus.getDriverId(),driverUserWorkStatus.getWorkStatus());
    }

}
