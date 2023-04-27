package com.brain.servicedriveruser.controller;


import com.brain.servicedriveruser.service.ICarService;
import com.brain.servicepassengeruser.internalcommon.dto.Car;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class CarController {

    @Autowired
    ICarService iCarService;

    @RequestMapping("/test")
    ResponseResult test(){

        return iCarService.test();
    }

    @RequestMapping("/car")
    ResponseResult addCar(@RequestBody Car car){

        return iCarService.addCar(car);
    }

}
