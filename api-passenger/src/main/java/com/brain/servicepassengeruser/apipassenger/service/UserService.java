package com.brain.servicepassengeruser.apipassenger.service;

import com.brain.servicepassengeruser.apipassenger.remote.ServicePassengerUsersClient;
import com.brain.servicepassengeruser.internalcommon.dto.PassengerUser;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.dto.TokenResult;
import com.brain.servicepassengeruser.internalcommon.util.JwtUtils;
import com.google.errorprone.annotations.SuppressPackageLocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
@Slf4j
public class UserService {

    @Autowired
    private ServicePassengerUsersClient servicePassengerUsersClient;

    public ResponseResult getUserByAccessToken(String accessToken){
        log.info("accessToken:"+accessToken);
        //解析accessToken，拿到手机号
        TokenResult tokenResult = JwtUtils.tokenCheck(accessToken);
        String phone = tokenResult.getPhone();
        log.info("手机号："+phone);
        //根据手机号查询用户信息
        ResponseResult userByPhone = servicePassengerUsersClient.getUserByPhone(phone);

        return ResponseResult.success(userByPhone.getData());
    }
}
