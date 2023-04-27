package com.brain.servicedriveruser.service;

import com.brain.servicedriveruser.mapper.DriverUserWorkStatusMapper;
import com.brain.servicepassengeruser.internalcommon.constant.CommonStatusEnum;
import com.brain.servicepassengeruser.internalcommon.constant.DriverCarConstants;
import com.brain.servicepassengeruser.internalcommon.dto.DriverUserWorkStatus;
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
 * @since 2023-04-27
 */


@Service
public class IDriverUserWorkStatusService{

    @Autowired
    DriverUserWorkStatusMapper driverUserWorkStatusMapper;

    public ResponseResult updateWorkState(Long driverId,Integer workStatus){
        LocalDateTime now = LocalDateTime.now();

        Map<String,Object> map = new HashMap<>();
        map.put("driver_id",driverId);

        List<DriverUserWorkStatus> driverUserWorkStatuses = driverUserWorkStatusMapper.selectByMap(map);
        if(driverUserWorkStatuses.size()!=1){
            return ResponseResult.fail(CommonStatusEnum.WORK_STATUS_NOT_EXISTS.getCode(),CommonStatusEnum.WORK_STATUS_NOT_EXISTS.getValue());
        }
        DriverUserWorkStatus driverUserWorkStatus1 = driverUserWorkStatuses.get(0);
        driverUserWorkStatus1.setWorkStatus(workStatus);
        driverUserWorkStatusMapper.updateById(driverUserWorkStatus1);

        return ResponseResult.success("");
    }

}
