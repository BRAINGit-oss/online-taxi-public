package com.brain.servicemap.controller;

import com.brain.servicemap.mapper.DicDistrictMapper;
import com.brain.servicepassengeruser.internalcommon.dto.DicDistrict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    DicDistrictMapper dicDistrictMapper;

    @GetMapping("/test-map")
    public String test(){

        Map<String,Object> map = new HashMap<>();
        map.put("address_code","110000");
        List<DicDistrict> dicDistricts = dicDistrictMapper.selectByMap(map);
        System.out.println(dicDistricts.toString());
        return "test-map db";
    }
}
