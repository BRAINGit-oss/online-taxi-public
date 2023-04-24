package com.brain.servicepassengeruser.apipassenger.service;

import com.brain.servicepassengeruser.internalcommon.constant.CommonStatusEnum;
import com.brain.servicepassengeruser.internalcommon.constant.TokenTypeConstants;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.dto.TokenResult;
import com.brain.servicepassengeruser.internalcommon.response.TokenResponse;
import com.brain.servicepassengeruser.internalcommon.util.JwtUtils;
import com.brain.servicepassengeruser.internalcommon.util.RedisPrefixUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class TokenService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public ResponseResult refreshSrc(String refreshToken){
        //解析refreshtoken 组成 String phone,String identity,String accessToken
        TokenResult tokenResult = JwtUtils.tokenCheck(refreshToken);
        if(tokenResult == null){
            return ResponseResult.fail(CommonStatusEnum.TOKEN_ERROR.getCode(),CommonStatusEnum.TOKEN_ERROR.getValue());
        }
        String phone = tokenResult.getPhone();
        String identity = tokenResult.getIdentity();

        //去读取redis 的refreshToken
        String refreshTokenKey = RedisPrefixUtils.generatorTokenKey(phone, identity, TokenTypeConstants.REFRESH_TOKEN_TYPE);
        String generatorRefreshToken = stringRedisTemplate.opsForValue().get(refreshTokenKey);

        //校验refreshToken
        if((StringUtils.isBlank(generatorRefreshToken)) || (!refreshToken.trim().equals(generatorRefreshToken.trim()))){
            return ResponseResult.fail(CommonStatusEnum.TOKEN_ERROR.getCode(),CommonStatusEnum.TOKEN_ERROR.getValue());
        }

        //生成双token
        String accessToken = JwtUtils.generatorToken(phone, identity, TokenTypeConstants.ACCESS_TOKEN_TYPE);
        String refreshToken2 = JwtUtils.generatorToken(phone, identity, TokenTypeConstants.REFRESH_TOKEN_TYPE);

        String accessTokenKey = RedisPrefixUtils.generatorTokenKey(phone, identity, TokenTypeConstants.ACCESS_TOKEN_TYPE);
//        stringRedisTemplate.opsForValue().set(accessTokenKey,accessToken,30, TimeUnit.DAYS);
//        stringRedisTemplate.opsForValue().set(refreshTokenKey,refreshToken,31,TimeUnit.DAYS);

        stringRedisTemplate.opsForValue().set(accessTokenKey,accessToken,10, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(refreshTokenKey,refreshToken,50,TimeUnit.SECONDS);



        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setRefreshToken(refreshToken2);
        tokenResponse.setAccessToken(accessToken);

        return ResponseResult.success(tokenResponse);
    }
}
