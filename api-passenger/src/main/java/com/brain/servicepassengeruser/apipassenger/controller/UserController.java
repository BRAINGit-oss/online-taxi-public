package com.brain.servicepassengeruser.apipassenger.controller;

import com.brain.servicepassengeruser.apipassenger.service.UserService;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/users")
    public ResponseResult userInfo(HttpServletRequest httpServletRequest){
        //从http请求中获取accessToken
        String authorizationAccessToken = httpServletRequest.getHeader("Authorization");

        return userService.getUserByAccessToken(authorizationAccessToken);
    }
}
