package com.brain.servicemap.controller;

import com.brain.servicemap.service.CreateService;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service")
public class ServiceController {

    @Autowired
    CreateService createService;

    @PostMapping("/add")
    ResponseResult add(@RequestParam String name){

        return createService.add(name);
    }
}
