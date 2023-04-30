package com.brain.servicemap.remote;

import com.brain.servicepassengeruser.internalcommon.constant.AmapConfigurationConstants;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.response.ServiceResponse;
import com.brain.servicepassengeruser.internalcommon.response.TerminalResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MapTerminalClinet {

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

    /**
     * 终端创建字符串拼接，调用高德API（restTemplate）,解析JSON，获取tid,返回TerminalResponse对象
     */

    public ResponseResult<TerminalResponse> addTerminal(String terminalName,String desc){

        /**
         *https://tsapi.amap.com/v1/track/terminal/add
         * ?
         * key=eb836633b2fe5b0c12f7f5323b79046c
         * &
         * sid=932958
         * &
         * name=车辆1
         *
         */
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(AmapConfigurationConstants.TERMINAL_ADD);
        stringBuilder.append("?");
        stringBuilder.append("key="+amapKey);
        stringBuilder.append("&");
        stringBuilder.append("sid="+amapSid);
        stringBuilder.append("&");
        stringBuilder.append("name="+terminalName);
        stringBuilder.append("&");
        stringBuilder.append("desc="+desc);


        log.info(stringBuilder.toString());
        //调用高德接口
        ResponseEntity<String> terminalEntity = restTemplate.postForEntity(stringBuilder.toString(),null, String.class);
        String directionString = terminalEntity.getBody();
        log.info("高德地图：路径规划，返回信息："+directionString);

        /**
         * {
         *     "data": {
         *         "name": "车辆1",
         *         "tid": 678404753,
         *         "sid": 932958
         *     },
         *     "errcode": 10000,
         *     "errdetail": null,
         *     "errmsg": "OK"
         * }
         */
        //解析JSON
        JSONObject terminalObject = JSONObject.fromObject(directionString);
        JSONObject dataTerminal = terminalObject.getJSONObject(AmapConfigurationConstants.DATA);
        String tid = dataTerminal.getString(AmapConfigurationConstants.TID);

        TerminalResponse terminalResponse = new TerminalResponse();
        terminalResponse.setTid(tid);

        return ResponseResult.success(terminalResponse);
    }

    public ResponseResult<List<TerminalResponse>> aroundSearch(String center,Integer radius){

        /**
         *https://tsapi.amap.com/v1/track/terminal/aroundsearch
         * ?
         * key=eb836633b2fe5b0c12f7f5323b79046c
         * &
         * sid=933518
         * &
         * center=39.915222,116.444007
         * &
         * radius=5000
         *
         */
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(AmapConfigurationConstants.TERMINAL_UPLOAD);
        stringBuilder.append("?");
        stringBuilder.append("key="+amapKey);
        stringBuilder.append("&");
        stringBuilder.append("sid="+amapSid);
        stringBuilder.append("&");
        stringBuilder.append("center="+center);
        stringBuilder.append("&");
        stringBuilder.append("radius="+radius);

        log.info(stringBuilder.toString());
        //调用高德接口
        ResponseEntity<String> terminalEntity = restTemplate.postForEntity(stringBuilder.toString(),null, String.class);
        String directionString = terminalEntity.getBody();
        log.info("高德地图：路径规划，返回信息："+directionString);

        /**
         * {
         *     "data": {
         *         "count": 3,
         *         "results": [
         *             {
         *                 "createtime": 1682844743258,
         *                 "desc": "234152342",
         *                 "locatetime": 1682843886644,
         *                 "location": {
         *                     "accuracy": 550.0,
         *                     "direction": 511.0,
         *                     "distance": 0,
         *                     "height": null,
         *                     "latitude": 39.915222,
         *                     "longitude": 116.444007,
         *                     "speed": 255.0,
         *                     "trackProps": null
         *                 },
         *                 "name": "车辆4",
         *                 "props": null,
         *                 "tid": 679730762
         *             }
         *         ]
         *     },
         *     "errcode": 10000,
         *     "errdetail": null,
         *     "errmsg": "OK"
         * }
         */
        //解析JSON
        JSONObject terminalObject = JSONObject.fromObject(directionString);
        JSONObject dataTerminal = terminalObject.getJSONObject(AmapConfigurationConstants.DATA);
        JSONArray results = dataTerminal.getJSONArray("results");

        List<TerminalResponse> list = new ArrayList<>();
        for(int i=0;i<results.size();i++){
            TerminalResponse terminalResponse = new TerminalResponse();
            JSONObject jsonObject = results.getJSONObject(i);
            Long desc = jsonObject.getLong("desc");
            String tid = jsonObject.getString(AmapConfigurationConstants.TID);

            terminalResponse.setTid(tid);
            terminalResponse.setCarId(desc);

            list.add(terminalResponse);
        }

        return ResponseResult.success(list);
    }
}
