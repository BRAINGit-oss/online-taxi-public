package com.brain.apiboss.controller;

import com.brain.apiboss.service.BossCarService;
import com.brain.servicepassengeruser.internalcommon.dto.Car;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarController {

    @Autowired
    BossCarService carService;

    @PostMapping("/car")
    ResponseResult addCar(@RequestBody Car car){
        return carService.addCarService(car);
    }
}
