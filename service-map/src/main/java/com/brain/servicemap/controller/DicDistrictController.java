package com.brain.servicemap.controller;

import com.brain.servicemap.remote.MapDicDistrictClient;
import com.brain.servicemap.service.DicDistrictService;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DicDistrictController {

    @Autowired
    DicDistrictService dicDistrictService;


    @GetMapping("/dicdistict")
    public ResponseResult initDicDistrict(String keywords){

        return dicDistrictService.initDicDistrict(keywords);
    }
}
