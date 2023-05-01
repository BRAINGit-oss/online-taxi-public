package com.brain.servicedriveruser.controller;


import com.brain.servicedriveruser.service.ICarService;
import com.brain.servicepassengeruser.internalcommon.dto.Car;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.ApiDriverRequest;
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

    /**
     * 创建车辆信息 同时添加tid(终端id)&trid(轨迹id)
     * @param car 车辆信息
     * @return success
     */
    @RequestMapping("/car")
    ResponseResult addCar(@RequestBody Car car){

        return iCarService.addCar(car);
    }

    /**
     * 通过id获取车辆信息
     * @param apiDriverRequest [carId,points[location,lacationtime]]
     * @return car
     */
    @RequestMapping("/carId")
    ResponseResult<Car> getCarById(@RequestBody ApiDriverRequest apiDriverRequest){

        return iCarService.getCarById(apiDriverRequest);
    }

}
