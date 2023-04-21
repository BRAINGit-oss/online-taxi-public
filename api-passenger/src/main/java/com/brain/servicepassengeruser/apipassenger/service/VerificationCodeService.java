package com.brain.servicepassengeruser.apipassenger.service;

import com.brain.servicepassengeruser.apipassenger.remote.ServicePassengerUsersClient;
import com.brain.servicepassengeruser.apipassenger.remote.ServiceVerificationcodeClient;
import com.brain.servicepassengeruser.internalcommon.constant.CommonStatusEnum;
import com.brain.servicepassengeruser.internalcommon.constant.IdentityConstant;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.VerificationCodeDTO;
import com.brain.servicepassengeruser.internalcommon.response.NumberCodeResponse;
import com.brain.servicepassengeruser.internalcommon.response.TokenResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.brain.servicepassengeruser.internalcommon.util.JwtUtils;


@Service
public class VerificationCodeService {
    @Autowired
    private ServiceVerificationcodeClient serviceVerificationcodeClient;

    @Autowired
    private ServicePassengerUsersClient servicePassengerUsersClient;

    //乘客验证码的前缀
    private String verificationCodePrefix = "passenger-verification-code-";

    @Autowired
    private StringRedisTemplate stringRedisTemplate; //字符串类型的

    /**
     * 生成验证码
     * @param passengerPhone 手机号
     * @return
     */
    public ResponseResult generatorCode(String passengerPhone){
        //调用验证码服务，获取验证码

        ResponseResult<NumberCodeResponse> numberCodeResponse = serviceVerificationcodeClient.getNumberCode(6);
        int numberCode = numberCodeResponse.getData().getNumberCode();
        System.out.println("remote number code:"+numberCode);

        // key,value,ttl过期时间
        String key = generatorKeyByPhone(passengerPhone);
        //存入redis
        stringRedisTemplate.opsForValue().set(key, numberCode+"",2, TimeUnit.MINUTES);

        //通过短信服务商，将对应的验证码发送到手机上，阿里短信服务，腾信短信通，华信，容联

        //返回值
//        JSONObject result = new JSONObject();
//        result.put("code",1);
//        result.put("message","success");
        return ResponseResult.success("");
    }

    private String generatorKeyByPhone(String passengerPhone){
        return verificationCodePrefix + passengerPhone;
    }

    /**
     * 校验验证码
     * @param passengerPhone 手机号
     * @param verificationCode 验证码
     * @return
     */
    public ResponseResult checkCode(String passengerPhone,String verificationCode){
        //根据手机号，去redis读取验证码
        //生成key
        String key = generatorKeyByPhone(passengerPhone);
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

        //判断是否有用户，并进行对应的处理
//        System.out.println("判断是否有用户，并进行对应的处理");
        VerificationCodeDTO verificationCodeDTO = new VerificationCodeDTO();
        verificationCodeDTO.setPassengerPhone(passengerPhone);
        servicePassengerUsersClient.getNumberCode(verificationCodeDTO);

        //颁发令牌 JWT Json Web Token 且可以验证令牌是否被修改过 如何验证？服务端颁发的token 可验证是否是服务端的颁发的
        System.out.println("颁发令牌");
        String s = JwtUtils.generatorToken(passengerPhone, IdentityConstant.PASSENGER_IDENTITY);

        //响应
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken("token value");
        return ResponseResult.success(tokenResponse);
    }


}
