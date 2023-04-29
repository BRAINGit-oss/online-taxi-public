package com.brain.servicemap.remote;

import com.brain.servicepassengeruser.internalcommon.constant.AmapConfigurationConstants;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.response.TerminalResponse;
import com.brain.servicepassengeruser.internalcommon.response.TrackResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class MapTrackClient {

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

    public ResponseResult<TrackResponse> addTrack(String tid){

        /**
         * https://tsapi.amap.com/v1/track/trace/add
         * ?
         * key=eb836633b2fe5b0c12f7f5323b79046c
         * &
         * sid=933518
         * &
         * tid=678476362
         */
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(AmapConfigurationConstants.TRACK_ADD);
        stringBuilder.append("?");
        stringBuilder.append("key="+amapKey);
        stringBuilder.append("&");
        stringBuilder.append("sid="+amapSid);
        stringBuilder.append("&");
        stringBuilder.append("tid="+tid);
//        stringBuilder.append("&");
//        stringBuilder.append("name="+terminalName);


        log.info(stringBuilder.toString());
        //调用高德接口
        ResponseEntity<String> trackEntity = restTemplate.postForEntity(stringBuilder.toString(),null, String.class);
        String trackString = trackEntity.getBody();
        log.info("高德地图：路径规划，返回信息："+trackString);

        /**
         * {
         *     "data": {
         *         "trid": 20
         *     },
         *     "errcode": 10000,
         *     "errdetail": null,
         *     "errmsg": "OK"
         * }
         */
        //解析JSON
        JSONObject trackObject = JSONObject.fromObject(trackString);
        JSONObject dataTerminal = trackObject.getJSONObject(AmapConfigurationConstants.DATA);
        String trid = dataTerminal.getString(AmapConfigurationConstants.TRID);
        String trname = "";

        if(dataTerminal.has("trname")){
            trname = dataTerminal.getString("trname");
        }

        TrackResponse trackResponse = new TrackResponse();
        trackResponse.setTrid(trid);
        trackResponse.setTrname(trname);


        return ResponseResult.success(trackResponse);
    }
}
