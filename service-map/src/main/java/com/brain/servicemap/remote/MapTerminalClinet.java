package com.brain.servicemap.remote;

import com.brain.servicepassengeruser.internalcommon.constant.AmapConfigurationConstants;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.response.ServiceResponse;
import com.brain.servicepassengeruser.internalcommon.response.TerminalResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

}
