package com.brain.servicedriveruser.service;

import com.brain.servicedriveruser.mapper.DriverUserMapper;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityDriverUserService {
    @Autowired
    DriverUserMapper driverUserMapper;
    public ResponseResult<Boolean> isAvailibalUser(String cityCode){
        int i = driverUserMapper.selectDriverUserCountByCityCode(cityCode);
        if(i>0){
            return ResponseResult.success(true);
        }else{
            return ResponseResult.success(false);
        }
    }
}
