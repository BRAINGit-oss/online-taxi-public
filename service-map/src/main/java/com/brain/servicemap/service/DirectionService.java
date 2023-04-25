package com.brain.servicemap.service;

import com.brain.servicemap.remote.MapDirectionClient;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.response.DirectionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DirectionService {

    @Autowired
    MapDirectionClient mapDirectionClient;

    public ResponseResult driving(String depLongitude,String depLatitude,String desLongitude,String desLatitude){
        //根据出发点&目的地的经纬度计算价格 调用计价服务
        log.info("调用地图服务，查询距离和时长");
        //调用第三方接口
        DirectionResponse direction = mapDirectionClient.direction(depLongitude, depLatitude, desLongitude, desLatitude);

        return ResponseResult.success(direction);
    }
}
