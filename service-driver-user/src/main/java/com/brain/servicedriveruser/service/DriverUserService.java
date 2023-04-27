package com.brain.servicedriveruser.service;

import com.brain.servicedriveruser.mapper.DriverUserMapper;
import com.brain.servicedriveruser.mapper.DriverUserWorkStatusMapper;
import com.brain.servicepassengeruser.internalcommon.constant.CommonStatusEnum;
import com.brain.servicepassengeruser.internalcommon.constant.DriverCarConstants;
import com.brain.servicepassengeruser.internalcommon.dto.DriverUser;
import com.brain.servicepassengeruser.internalcommon.dto.DriverUserWorkStatus;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.response.CheckPhoneResponse;
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

    public ResponseResult getDriverUserByPhone(String driverPhone){

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
}
