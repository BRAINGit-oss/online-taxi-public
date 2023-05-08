package com.brain.servicemap.remote;

import com.brain.servicepassengeruser.internalcommon.constant.AmapConfigurationConstants;
import com.brain.servicepassengeruser.internalcommon.response.DirectionResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class MapDirectionClient {

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
     * 根据始发地经纬度&目的地经纬度获取距离和时长
     * @param depLongitude 始发地经度
     * @param depLatitude 始发地纬度
     * @param desLongitude 目的地经度
     * @param desLatitude 目的地纬度
     * @return
     */
    public DirectionResponse direction(String depLongitude,String depLatitude,String desLongitude,String desLatitude){

        /**
         *https://restapi.amap.com/v3/direction/driving?origin=116.45925,39.910031&destination=116.587922,40.081577&output=json&key=eb836633b2fe5b0c12f7f5323b79046c
         */
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(AmapConfigurationConstants.DIRERECTION_URL);
        stringBuilder.append("?");
        stringBuilder.append("origin="+depLongitude+","+depLatitude);
        stringBuilder.append("&");
        stringBuilder.append("destination="+desLongitude+","+desLatitude);
        stringBuilder.append("&");
        stringBuilder.append("output=json");
        stringBuilder.append("&");
        stringBuilder.append("key="+amapKey);
//        log.info(stringBuilder.toString());
        //调用高德接口
        log.info("高德地图：路径规划，请求信息："+stringBuilder.toString());
        ResponseEntity<String> directionEntity = restTemplate.getForEntity(stringBuilder.toString(), String.class);
        String directionString = directionEntity.getBody();
        log.info("高德地图：路径规划，返回信息："+directionString);

        //解析接口
        DirectionResponse directionResponse = parseDirectionEntity(directionString);

        return directionResponse;
    }

    /**
     * 具体解析获取距离：米 时长：分 信息
     * @param directionEntity directionEntity.getBody()后的String类型对象
     * @return DirectionResponse
     */
    public DirectionResponse parseDirectionEntity(String directionEntity){
        //解析接口
        DirectionResponse directionResponse = null;
        try{
            //解析最外层JSON
            JSONObject result = JSONObject.fromObject(directionEntity);
            int statusCode = result.getInt(AmapConfigurationConstants.STATUS);
            if(statusCode == 1){
                if(result.has(AmapConfigurationConstants.ROUTE)){
                    JSONObject routeObject = result.getJSONObject(AmapConfigurationConstants.ROUTE);
                    JSONArray pathArray = routeObject.getJSONArray(AmapConfigurationConstants.PATHS);
                    JSONObject pathObjectJSONObject = pathArray.getJSONObject(0);
                    directionResponse = new DirectionResponse();
                    // 最外层->route->paths->paths[0]->distance,duration
                    //获取distance和duration信息，返回DirectionResponse对象
                    if(pathObjectJSONObject.has(AmapConfigurationConstants.DISTANCE)){
                        int distance = pathObjectJSONObject.getInt(AmapConfigurationConstants.DISTANCE);
                        directionResponse.setDistance(distance);
                    }
                    if(pathObjectJSONObject.has(AmapConfigurationConstants.DURATION)){
                        int duration = pathObjectJSONObject.getInt((AmapConfigurationConstants.DURATION));
                        directionResponse.setDuration(duration);
                    }
                }
            }

        }catch (Exception e){

        }

        return directionResponse;
    }
}
