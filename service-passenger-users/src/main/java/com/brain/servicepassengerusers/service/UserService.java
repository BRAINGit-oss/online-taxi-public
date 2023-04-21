package com.brain.servicepassengerusers.service;

import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengerusers.dto.PassengerUser;
import com.brain.servicepassengerusers.mapper.PassengerUserMapper;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private PassengerUserMapper passengerUserMapper;

    public ResponseResult loginOrRegistor(String passengerPhone) {
        System.out.println("user service被调用，手机号："+passengerPhone);

        //根据手机号查询用户信息
        Map<String,Object> map = new HashMap<>();
        map.put("passenger_phone",passengerPhone);
        List<PassengerUser> passengerUserList = passengerUserMapper.selectByMap(map);
        System.out.println(passengerUserList.size() == 0 ? "无记录":passengerUserList.get(0).getPassengerName());


        //判断用户信息是否存在

        //如果不存在，插入用户信息

        return ResponseResult.success();
    }
}
