package com.brain.servicemap.remote;

import com.brain.servicepassengeruser.internalcommon.constant.AmapConfigurationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MapDicDistrictClient {

    @Value("${amap.key}")
    private String amapKey;

    @Autowired
    private RestTemplate restTemplate;

    public String dicDistrict(String keywords){
        //行政区url拼接
        //?keywords=北京&subdistrict=2&key=<用户的key>
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(AmapConfigurationConstants.DISTRICT_URL);
        stringBuilder.append("?");
        stringBuilder.append("keywords="+keywords);
        stringBuilder.append("&");
        stringBuilder.append("subdistrict=3");
        stringBuilder.append("&");
        stringBuilder.append("key="+amapKey);
        //调用高德接口
        ResponseEntity<String> districtEntity = restTemplate.getForEntity(stringBuilder.toString(), String.class);

        return districtEntity.getBody();
    }

}
