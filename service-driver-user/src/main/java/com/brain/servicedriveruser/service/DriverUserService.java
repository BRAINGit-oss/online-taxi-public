package com.brain.servicedriveruser.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.brain.servicedriveruser.mapper.CarMapper;
import com.brain.servicedriveruser.mapper.DriverCarBindingRelationshipMapper;
import com.brain.servicedriveruser.mapper.DriverUserMapper;
import com.brain.servicedriveruser.mapper.DriverUserWorkStatusMapper;
import com.brain.servicepassengeruser.internalcommon.constant.CommonStatusEnum;
import com.brain.servicepassengeruser.internalcommon.constant.DriverCarConstants;
import com.brain.servicepassengeruser.internalcommon.dto.*;
import com.brain.servicepassengeruser.internalcommon.response.CheckPhoneResponse;
import com.brain.servicepassengeruser.internalcommon.response.DriverWorkStatusResponse;
import com.mysql.cj.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DriverUserService {

    @Autowired
    DriverUserMapper driverUserMapper;

    @Autowired
    DriverUserWorkStatusMapper driverUserWorkStatusMapper;

    @Autowired
    DriverCarBindingRelationshipMapper driverCarBindingRelationshipMapper;

    @Autowired
    CarMapper carMapper;

    public ResponseResult testUser(){

        DriverUser driverUser = driverUserMapper.selectById(1);

        return ResponseResult.success(driverUser);
    }

    public ResponseResult addUser(DriverUser driverUser) {
        LocalDateTime now = LocalDateTime.now();
        driverUser.setGmtCreate(now);
        driverUser.setGmtModify(now);

        driverUserMapper.insert(driverUser);

        //初始化司机工作状态表 可以获取driver_user表的id，因为上面插入后会自动生成id
        DriverUserWorkStatus driverUserWorkStatus = new DriverUserWorkStatus();
        driverUserWorkStatus.setDriverId(driverUser.getId());
        driverUserWorkStatus.setWorkStatus(DriverCarConstants.DRIVER_WORK_STOP);
        driverUserWorkStatus.setGmtModified(now);
        driverUserWorkStatus.setGmtCreate(now);

        driverUserWorkStatusMapper.insert(driverUserWorkStatus);

        return ResponseResult.success("");
    }

    public ResponseResult updateUser(DriverUser driverUser){
        LocalDateTime now = LocalDateTime.now();
        driverUser.setGmtModify(now);
        driverUserMapper.updateById(driverUser);
        return ResponseResult.success("");
    }

    public ResponseResult<DriverUser> getDriverUserByPhone(String driverPhone){

        Map<String,Object> map = new HashMap<>();
        map.put("driver_phone",driverPhone);
        map.put("state", DriverCarConstants.DRIVER_STATUS_YES);
        List<DriverUser> driverUsers = driverUserMapper.selectByMap(map);
        if(driverUsers.isEmpty()){
            return ResponseResult.fail(CommonStatusEnum.DRIVER_STATUS_UNVALID.getCode(),CommonStatusEnum.DRIVER_STATUS_UNVALID.getValue());
        }
        DriverUser driverUser = driverUsers.get(0);
        log.info(driverPhone+" 司机号存在");
        return ResponseResult.success(driverUser);
    }

    /**
     * 根据driver_car_binding_relationship表的car_id查询可用的司机信息
     * @param carId 车辆ID
     * @return DriverWorkStatusResponse car_id,driver_id,driver_phone
     */
    public ResponseResult<DriverWorkStatusResponse> getAvailableDriverByCarId(Long carId){

        //根据carID+绑定状态查询 得到driverId 司机车辆绑定表
        QueryWrapper<DriverCarBindingRelationship> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("car_id",carId);
        objectQueryWrapper.eq("state",DriverCarConstants.DRIVER_CAR_BIND);
        DriverCarBindingRelationship driverCarBindingRelationship = driverCarBindingRelationshipMapper.selectOne(objectQueryWrapper);
        if(null == driverCarBindingRelationship){
            return ResponseResult.fail(CommonStatusEnum.CAR_WORK_EMPTY.getCode(),CommonStatusEnum.CAR_WORK_EMPTY.getValue());
        }
        Long driverId = driverCarBindingRelationship.getDriverId();

        //根据driverId+出车状态查询 判断是否有可用车辆 司机车辆工作状态表
        QueryWrapper<DriverUserWorkStatus> objectQueryWrapper1 = new QueryWrapper<>();
        objectQueryWrapper1.eq("driver_id",driverId);
        objectQueryWrapper1.eq("work_status",DriverCarConstants.DRIVER_WORK_START);
        DriverUserWorkStatus driverUserWorkStatus = driverUserWorkStatusMapper.selectOne(objectQueryWrapper1);
        DriverWorkStatusResponse driverWorkStatusResponse = new DriverWorkStatusResponse();
        if(null == driverUserWorkStatus){
            return ResponseResult.fail(CommonStatusEnum.DRIVER_WORK_EMPTY.getCode(),CommonStatusEnum.DRIVER_WORK_EMPTY.getValue());
        }else{
            //根据driverId得到driverPhone 司机信息表
            QueryWrapper<DriverUser> driveridQueryResult = new QueryWrapper<>();
            driveridQueryResult.eq("id",driverId);
            DriverUser driverUser = driverUserMapper.selectOne(driveridQueryResult);

            QueryWrapper<Car> carQueryResult = new QueryWrapper<>();
            carQueryResult.eq("id",carId);
            Car car = carMapper.selectOne(carQueryResult);


            driverWorkStatusResponse.setCarId(carId);
            driverWorkStatusResponse.setDriverId(driverId);
            driverWorkStatusResponse.setDriverPhone(driverUser.getDriverPhone());
            driverWorkStatusResponse.setLicenseId(driverUser.getLicenseId());
            driverWorkStatusResponse.setVehicleNo(car.getVehicleNo());


            return ResponseResult.success(driverWorkStatusResponse);
        }

    }
}
