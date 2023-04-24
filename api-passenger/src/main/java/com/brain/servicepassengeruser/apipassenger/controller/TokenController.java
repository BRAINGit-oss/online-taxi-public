package com.brain.servicepassengeruser.apipassenger.controller;

import com.brain.servicepassengeruser.apipassenger.service.TokenService;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.dto.TokenResult;
import com.brain.servicepassengeruser.internalcommon.response.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @PostMapping("/token-refresh")
    public ResponseResult refreshTokenGetAccessToken(@RequestBody TokenResponse tokenResponse){

        String refreshToken = tokenResponse.getRefreshToken();
        System.out.println("original refreshToken is :"+refreshToken);

        return tokenService.refreshSrc(refreshToken);
    }
}
