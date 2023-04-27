package com.brain.servicedriveruser.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.brain.servicedriveruser.mapper.DriverCarBindingRelationshipMapper;
import com.brain.servicepassengeruser.internalcommon.constant.CommonStatusEnum;
import com.brain.servicepassengeruser.internalcommon.constant.DriverCarConstants;
import com.brain.servicepassengeruser.internalcommon.dto.Car;
import com.brain.servicepassengeruser.internalcommon.dto.DriverCarBindingRelationship;
import com.baomidou.mybatisplus.extension.service.IService;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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
public class IDriverCarBindingRelationshipService{

    @Autowired
    DriverCarBindingRelationshipMapper driverCarBindingRelationshipMapper;

    public ResponseResult bindService(DriverCarBindingRelationship driverCarBindingRelationship){
        QueryWrapper<DriverCarBindingRelationship> queryWrapper = new QueryWrapper();
        queryWrapper.eq("driver_id",driverCarBindingRelationship.getDriverId());
        queryWrapper.eq("car_id",driverCarBindingRelationship.getCarId());
        queryWrapper.eq("state", DriverCarConstants.DRIVER_CAR_BIND);

        Integer recordCount = driverCarBindingRelationshipMapper.selectCount(queryWrapper);
        if((recordCount.intValue()>0)){
            return ResponseResult.fail(CommonStatusEnum.DRIVER_CAR_EXISTS.getCode(),CommonStatusEnum.DRIVER_CAR_EXISTS.getValue());
        }

        queryWrapper = new QueryWrapper();
        queryWrapper.eq("driver_id",driverCarBindingRelationship.getDriverId());
        queryWrapper.eq("state", DriverCarConstants.DRIVER_CAR_BIND);

        Integer recordDriverCount = driverCarBindingRelationshipMapper.selectCount(queryWrapper);
        if((recordDriverCount.intValue()>0)){
            return ResponseResult.fail(CommonStatusEnum.DRIVER_EXISTS.getCode(),CommonStatusEnum.DRIVER_EXISTS.getValue());
        }

        queryWrapper = new QueryWrapper();
        queryWrapper.eq("car_id",driverCarBindingRelationship.getCarId());
        queryWrapper.eq("state", DriverCarConstants.DRIVER_CAR_BIND);

        Integer recordCarCount = driverCarBindingRelationshipMapper.selectCount(queryWrapper);
        if((recordCarCount.intValue()>0)){
            return ResponseResult.fail(CommonStatusEnum.CAR_EXISTS.getCode(),CommonStatusEnum.CAR_EXISTS.getValue());
        }

        //判断是否存在driverId&carId,若存在，则执行update state=1
        queryWrapper = new QueryWrapper();
        queryWrapper.eq("driver_id",driverCarBindingRelationship.getDriverId());
        queryWrapper.eq("car_id",driverCarBindingRelationship.getCarId());

        List<DriverCarBindingRelationship> driverCarBindingRelationships = driverCarBindingRelationshipMapper.selectList(queryWrapper);
        if((driverCarBindingRelationships.size()>0)){
            DriverCarBindingRelationship driverCarBindingRelationship1 = driverCarBindingRelationships.get(0);
            driverCarBindingRelationship1.setState(DriverCarConstants.DRIVER_CAR_BIND);
            driverCarBindingRelationshipMapper.updateById(driverCarBindingRelationship1);
            return ResponseResult.success("");
        }

        LocalDateTime now = LocalDateTime.now();
        driverCarBindingRelationship.setBindingTime(now);
        driverCarBindingRelationship.setState(DriverCarConstants.DRIVER_CAR_BIND);

        driverCarBindingRelationshipMapper.insert(driverCarBindingRelationship);
        return ResponseResult.success("");
    }

    public ResponseResult unBindService(DriverCarBindingRelationship driverCarBindingRelationship){

        Map<String,Object> map = new HashMap<>();
        map.put("driver_id",driverCarBindingRelationship.getDriverId());
        map.put("car_id",driverCarBindingRelationship.getCarId());
        map.put("state",DriverCarConstants.DRIVER_CAR_BIND);
        List<DriverCarBindingRelationship> driverCarBindingRelationships = driverCarBindingRelationshipMapper.selectByMap(map);
        //查询是否存在用户
        if(driverCarBindingRelationships.size() == 0){
            return ResponseResult.fail(CommonStatusEnum.DRIVER_CAR_NOT_EXISTS.getCode(),CommonStatusEnum.DRIVER_CAR_NOT_EXISTS.getValue());
        }
        //设置"未绑定"&"未绑定"时间
        DriverCarBindingRelationship relationship = driverCarBindingRelationships.get(0);
        LocalDateTime now = LocalDateTime.now();
        relationship.setUnBindingTime(now);
        relationship.setState(DriverCarConstants.DRIVER_CAR_UNBIND);

        driverCarBindingRelationshipMapper.updateById(relationship);

        return ResponseResult.success("");
    }

}
