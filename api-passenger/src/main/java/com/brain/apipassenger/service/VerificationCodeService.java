package com.brain.apipassenger.service;

import com.brain.apipassenger.remote.ServiceVerificationcodeClient;
import com.brain.internalcommon.dto.ResponseResult;
import com.brain.internalcommon.response.NumberCodeResponse;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class VerificationCodeService {
    @Autowired
    private ServiceVerificationcodeClient serviceVerificationcodeClient;

    //乘客验证码的前缀
    private String verificationCodePrefix = "passenger-verification-code-";

    @Autowired
    private StringRedisTemplate stringRedisTemplate; //字符串类型的

    public ResponseResult generatorCode(String passengerPhone){
        //调用验证码服务，获取验证码

        ResponseResult<NumberCodeResponse> numberCodeResponse = serviceVerificationcodeClient.getNumberCode(6);
        int numberCode = numberCodeResponse.getData().getNumberCode();
        System.out.println("remote number code:"+numberCode);

        // key,value,ttl过期时间
        String key = verificationCodePrefix+passengerPhone;
        //存入redis
        stringRedisTemplate.opsForValue().set(key, numberCode+"",2, TimeUnit.MINUTES);

        //通过短信服务商，将对应的验证码发送到手机上，阿里短信服务，腾信短信通，华信，容联

        //返回值
//        JSONObject result = new JSONObject();
//        result.put("code",1);
//        result.put("message","success");
        return ResponseResult.success("");
    }

}
