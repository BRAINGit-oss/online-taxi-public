package com.brain.servicemap.remote;

import com.brain.servicepassengeruser.internalcommon.constant.AmapConfigurationConstants;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.response.DirectionResponse;
import com.brain.servicepassengeruser.internalcommon.response.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class MapServiceClient {

    /**
     * 从application.yml 读取高地地图生成的key信息
     */
    @Value("${amap.key}")
    private String amapKey;

    /**
     * 调用高德地图API获取距离：米，时长：分 信息
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 服务创建字符串拼接，调用高德API（restTemplate）,解析JSON，获取sid,返回封装ServiceResponse对象
     */
    public ResponseResult<ServiceResponse> addService(String name){

        /**
         *https://tsapi.amap.com/v1/track/service/add
         * ?
         * key=eb836633b2fe5b0c12f7f5323b79046c
         * &
         * name=飞滴出行项目Service
         */
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(AmapConfigurationConstants.SERVICE_ADD);
        stringBuilder.append("?");
        stringBuilder.append("key="+amapKey);
        stringBuilder.append("&");
        stringBuilder.append("name="+name);

        log.info(stringBuilder.toString());
        //调用高德接口
        ResponseEntity<String> serviceEntity = restTemplate.postForEntity(stringBuilder.toString(),null, String.class);
        String directionString = serviceEntity.getBody();
        log.info("高德地图：路径规划，返回信息："+directionString);


        /**
         * {
         *     "data": {
         *         "name": "飞滴出行项目Service1",
         *         "sid": 929850
         *     },
         *     "errcode": 10000,
         *     "errdetail": null,
         *     "errmsg": "OK"
         * }
         */
        //解析JSON
        JSONObject serviceObject = JSONObject.fromObject(directionString);
        JSONObject dataService = serviceObject.getJSONObject(AmapConfigurationConstants.DATA);
        String sid = dataService.getString(AmapConfigurationConstants.SID);

        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setSid(sid);



        return ResponseResult.success(serviceResponse);
    }
}
