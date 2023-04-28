package com.brain.servicedriveruser.service;

import com.brain.servicedriveruser.mapper.CarMapper;
import com.brain.servicedriveruser.remote.TerminalClient;
import com.brain.servicepassengeruser.internalcommon.dto.Car;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.response.TerminalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Autowired
    TerminalClient terminalClient;

    public ResponseResult test(){
        com.brain.servicepassengeruser.internalcommon.dto.Car car = carMapper.selectById(1);
        return ResponseResult.success(car);
    }

    public ResponseResult addCar(Car car){
        LocalDateTime now = LocalDateTime.now();
        car.setGmtCreate(now);
        car.setGmtModify(now);

        //调用猎鹰API远程接口获取tid
        ResponseResult<TerminalResponse> serviceResponseResponseResult = terminalClient.addTerminal(car.getVehicleNo());
        String tid = serviceResponseResponseResult.getData().getTid();
        car.setTid(tid);

        carMapper.insert(car);
        return ResponseResult.success("");
    }

}
