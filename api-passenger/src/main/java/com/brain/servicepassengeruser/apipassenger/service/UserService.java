package com.brain.servicepassengeruser.apipassenger.service;

import com.brain.servicepassengeruser.internalcommon.dto.PassengerUser;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.google.errorprone.annotations.SuppressPackageLocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
@Slf4j
public class UserService {

    public static ResponseResult getUserByAccessToken(String accessToken){
        log.info("accessToken:"+accessToken);
        //解析accessToken，拿到手机号

        //根据手机号查询用户信息

        PassengerUser passengerUser = new PassengerUser();
        passengerUser.setPassengerName("张三");
        passengerUser.setProfilePhoto("头像");

        return ResponseResult.success(passengerUser);
    }
}
