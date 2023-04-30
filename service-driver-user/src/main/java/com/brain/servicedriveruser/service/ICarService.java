package com.brain.servicedriveruser.service;

import com.brain.servicedriveruser.mapper.CarMapper;
import com.brain.servicedriveruser.remote.ServiceMapClient;
import com.brain.servicepassengeruser.internalcommon.dto.Car;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.ApiDriverRequest;
import com.brain.servicepassengeruser.internalcommon.response.TerminalResponse;
import com.brain.servicepassengeruser.internalcommon.response.TrackResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
    ServiceMapClient terminalClient;

    public ResponseResult test(){
        com.brain.servicepassengeruser.internalcommon.dto.Car car = carMapper.selectById(1);
        return ResponseResult.success(car);
    }

    public ResponseResult addCar(Car car){
        LocalDateTime now = LocalDateTime.now();
        car.setGmtCreate(now);
        car.setGmtModify(now);

        carMapper.insert(car);

        //调用猎鹰API远程接口获取tid
        ResponseResult<TerminalResponse> serviceResponseResponseResult = terminalClient.addTerminal(car.getVehicleNo(),car.getId()+"");
        String tid = serviceResponseResponseResult.getData().getTid();
        car.setTid(tid);

        //调用猎鹰API远程接口获取trid
        ResponseResult<TrackResponse> trackResponseResponseResult = terminalClient.addTrack(tid);
        String trid = trackResponseResponseResult.getData().getTrid();
        String trname = trackResponseResponseResult.getData().getTrname();
        car.setTrid(trid);
        car.setTrname(trname);

        carMapper.updateById(car);
        return ResponseResult.success("");
    }

    public ResponseResult<Car> getCarById(ApiDriverRequest apiDriverRequest){
        Map<String,Object> map = new HashMap<>();
        map.put("id",apiDriverRequest.getCarId());
        Car car = carMapper.selectById((Serializable) map);

        return ResponseResult.success(car);
    }
}
