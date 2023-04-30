package com.brain.servicemap.remote;

import com.brain.servicepassengeruser.internalcommon.constant.AmapConfigurationConstants;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.PointsDTO;
import com.brain.servicepassengeruser.internalcommon.request.PointsRequest;
import com.brain.servicepassengeruser.internalcommon.response.TrackResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@Slf4j
public class MapPointClient {
    /**
     * 从application.yml 读取高地地图生成的key信息
     */
    @Value("${amap.key}")
    private String amapKey;

    @Value("${amap.sid}")
    private String amapSid;
    /**
     * 调用高德地图API获取距离：米，时长：分 信息
     */
    @Autowired
    private RestTemplate restTemplate;

    public ResponseResult pointUpload(PointsRequest pointsRequest){
        /**
         * https://tsapi.amap.com/v1/track/point/upload
         * ?
         * key=eb836633b2fe5b0c12f7f5323b79046c
         * &
         * sid=933518
         * &
         * tid=678748543
         * &
         * trid=80
         * &
         * points=[{"location":"116.444007,39.915222","locatetime":1544176895000}]
         * URL编码
         * [ %5B
         * { %7B
         * " %22
         * : %3A
         * , %2C
         * } %7D
         * ] %5D
         */
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigurationConstants.POINTS_UPLOAD);
        url.append("?");
        url.append("key="+amapKey);
        url.append("&");
        url.append("sid="+amapSid);
        url.append("&");
        url.append("tid="+pointsRequest.getTid());
        url.append("&");
        url.append("trid="+pointsRequest.getTrid());
        url.append("&");
        url.append("points=");
        PointsDTO[] points = pointsRequest.getPoints();
        url.append("%5B");
        for (PointsDTO p:points
             ) {
            String locatetime = p.getLocatetime();
            String location = p.getLocation();
            url.append("%7B");
            url.append("%22location%22");
            url.append("%3A");
            url.append("%22"+location+"%22");
            url.append("%2C");

            url.append("%22locatetime%22");
            url.append("%3A");
            url.append(locatetime);
            url.append("%7D");
        }
        url.append("%5D");


        log.info(url.toString());
        //调用高德接口
        ResponseEntity<String> pointEntity = restTemplate.postForEntity(URI.create(url.toString()),null, String.class);
        String pointString = pointEntity.getBody();
        log.info("高德地图：路径规划，返回信息："+pointString);

        return ResponseResult.success("");
    }
}
