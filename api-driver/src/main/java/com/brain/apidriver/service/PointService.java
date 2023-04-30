package com.brain.apidriver.service;

import com.brain.apidriver.remote.ServiceMapClient;
import com.brain.apidriver.remote.ServiceUserClient;
import com.brain.servicepassengeruser.internalcommon.dto.Car;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.ApiDriverRequest;
import com.brain.servicepassengeruser.internalcommon.request.PointsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    @Autowired
    ServiceMapClient serviceMapClient;

    @Autowired
    ServiceUserClient serviceUserClient;

    public ResponseResult pointUpload(ApiDriverRequest apiDriverRequest){
        //获取carId
//        Long carId = apiDriverRequest.getCarId();

        //根据carId获取tid,trid,调用service-driver-user接口
        ResponseResult<Car> carById = serviceUserClient.getCarById(apiDriverRequest);
        Car data = carById.getData();
        String tid = data.getTid();
        String trid = data.getTrid();


        //发送请求接口获取响应信息,调用service-map接口
        PointsRequest pointsRequest = new PointsRequest();
        pointsRequest.setTid(tid);
        pointsRequest.setTrid(trid);
        pointsRequest.setPoints(apiDriverRequest.getPoints());
        serviceMapClient.pointsUpload(pointsRequest);
        return ResponseResult.success("");
    }
}
