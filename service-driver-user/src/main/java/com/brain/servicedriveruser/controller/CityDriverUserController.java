package com.brain.servicedriveruser.controller;

import com.brain.servicedriveruser.service.CityDriverUserService;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/city-driver")
public class CityDriverUserController {
    @Autowired
    CityDriverUserService cityDriverUserService;

    @GetMapping("/is_available_driver")
    public ResponseResult<Boolean> isAvailableDriver(@RequestParam String cityCode){
        return cityDriverUserService.isAvailibalUser(cityCode);
    }
}
