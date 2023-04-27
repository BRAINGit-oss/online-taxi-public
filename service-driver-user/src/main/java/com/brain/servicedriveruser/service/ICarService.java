package com.brain.servicedriveruser.service;

import com.brain.servicedriveruser.mapper.CarMapper;
import com.brain.servicepassengeruser.internalcommon.dto.Car;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Brain
 * @since 2023-04-26
 */
@Service
public class ICarService{

    @Autowired
    public CarMapper carMapper;

    public ResponseResult test(){
        com.brain.servicepassengeruser.internalcommon.dto.Car car = carMapper.selectById(1);
        return ResponseResult.success(car);
    }

    public ResponseResult addCar(Car car){
        carMapper.insert(car);
        return ResponseResult.success("");
    }

}
