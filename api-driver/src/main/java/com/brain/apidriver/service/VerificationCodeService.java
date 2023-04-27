package com.brain.apidriver.service;

import com.brain.apidriver.remote.ServiceUserClient;
import com.brain.apidriver.remote.ServiceVerificationCodeClient;
import com.brain.servicepassengeruser.internalcommon.constant.CommonStatusEnum;
import com.brain.servicepassengeruser.internalcommon.constant.DriverCarConstants;
import com.brain.servicepassengeruser.internalcommon.constant.IdentityConstants;
import com.brain.servicepassengeruser.internalcommon.constant.TokenTypeConstants;
import com.brain.servicepassengeruser.internalcommon.dto.DriverUser;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.VerificationCodeDTO;
import com.brain.servicepassengeruser.internalcommon.response.CheckPhoneResponse;
import com.brain.servicepassengeruser.internalcommon.response.NumberCodeResponse;
import com.brain.servicepassengeruser.internalcommon.response.TokenResponse;
import com.brain.servicepassengeruser.internalcommon.util.JwtUtils;
import com.brain.servicepassengeruser.internalcommon.util.RedisPrefixUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class VerificationCodeService {

    @Autowired
    ServiceUserClient serviceUserClient;

    @Autowired
    ServiceVerificationCodeClient serviceVerificationCodeClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate; //字符串类型的

    public ResponseResult getVerificationCode(String driverPhone){
        //查询service-driver-user,该手机号的司机是否存在
        ResponseResult<CheckPhoneResponse> responseResult = serviceUserClient.checkPhone(driverPhone);
        CheckPhoneResponse data = responseResult.getData();
        String phone = data.getDriverPhone();
        Integer ifEmpty = responseResult.getData().getIfEmpty();
        if(ifEmpty == DriverCarConstants.DRIVER_STATUS_NO){
            return ResponseResult.fail(CommonStatusEnum.DRIVER_STATUS_UNVALID.getCode(),CommonStatusEnum.DRIVER_STATUS_UNVALID.getValue());
        }
        log.info(phone+" 的司机已存在");
        //获取验证码
        ResponseResult<NumberCodeResponse> verification = serviceVerificationCodeClient.getVerification(6);
        NumberCodeResponse codeResponse = verification.getData();
        int numberCode = codeResponse.getNumberCode();
        log.info("验证码为："+numberCode);
        //调用第三方发送验证码,第三方：阿里短信服务，腾讯，华信，容联

        //存入Redis 1、key 2、存入
        //建立key
        String key = RedisPrefixUtils.generatorKeyByPhone(phone, IdentityConstants.DRIVER_IDENTITY);
        stringRedisTemplate.opsForValue().set(key, String.valueOf(numberCode),2, TimeUnit.MINUTES);

        return ResponseResult.success("");
    }

    /**
     * 校验验证码
     * @param driverPhone 手机号
     * @param verificationCode 验证码
     * @return
     */
    public ResponseResult checkCode(String driverPhone,String verificationCode){
        //根据手机号，去redis读取验证码
        //生成key
        String key = RedisPrefixUtils.generatorKeyByPhone(driverPhone,IdentityConstants.DRIVER_IDENTITY);
        //根据key获取value
        String codeRedis = stringRedisTemplate.opsForValue().get(key);
        System.out.println("redis中的value:"+codeRedis);

        //校验验证码 key未过期 key已过期 提示：验证码已过期
        if(StringUtils.isBlank(codeRedis)){
            return ResponseResult.fail(CommonStatusEnum.VERIFICATION_CODE_ERROR.getCode(),CommonStatusEnum.VERIFICATION_CODE_ERROR.getValue());
        }
        if(!verificationCode.trim().equals(codeRedis.trim())){
            return ResponseResult.fail(CommonStatusEnum.VERIFICATION_CODE_ERROR.getCode(),CommonStatusEnum.VERIFICATION_CODE_ERROR.getValue());
        }

        //颁发令牌 JWT Json Web Token 且可以验证令牌是否被修改过 如何验证？服务端颁发的token 可验证是否是服务端的颁发的
//        System.out.println("颁发令牌");
        String accessToken = JwtUtils.generatorToken(driverPhone, IdentityConstants.DRIVER_IDENTITY, TokenTypeConstants.ACCESS_TOKEN_TYPE);
        //将token存到redis中
        String key1 = RedisPrefixUtils.generatorTokenKey(driverPhone, IdentityConstants.DRIVER_IDENTITY,TokenTypeConstants.ACCESS_TOKEN_TYPE);
        stringRedisTemplate.opsForValue().set(key1,accessToken,30,TimeUnit.DAYS);
//        stringRedisTemplate.opsForValue().set(key1,accessToken,10,TimeUnit.SECONDS);


        String refreshToken = JwtUtils.generatorToken(driverPhone, IdentityConstants.DRIVER_IDENTITY, TokenTypeConstants.REFRESH_TOKEN_TYPE);
        //将token存到redis中
        String key2 = RedisPrefixUtils.generatorTokenKey(driverPhone, IdentityConstants.DRIVER_IDENTITY,TokenTypeConstants.REFRESH_TOKEN_TYPE);
        stringRedisTemplate.opsForValue().set(key2,refreshToken,31,TimeUnit.DAYS);
//        stringRedisTemplate.opsForValue().set(key2,refreshToken,50,TimeUnit.SECONDS);


        //响应
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(accessToken);
        tokenResponse.setRefreshToken(refreshToken);
        return ResponseResult.success(tokenResponse);
    }
}
