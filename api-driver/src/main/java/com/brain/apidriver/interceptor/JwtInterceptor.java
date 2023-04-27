package com.brain.apidriver.interceptor;


import com.brain.servicepassengeruser.internalcommon.constant.TokenTypeConstants;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.dto.TokenResult;
import com.brain.servicepassengeruser.internalcommon.util.JwtUtils;
import com.brain.servicepassengeruser.internalcommon.util.RedisPrefixUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate; //字符串类型的

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        boolean result = true;
        String resultString = "";

        //从前端获取的token
        String token = request.getHeader("Authorization");
        //解析token
        TokenResult tokenResult = JwtUtils.tokenCheck(token);

        if(tokenResult ==null){
            resultString = "access token invalid";
            result = false;
        }else{
            //拼接key
            String phone = tokenResult.getPhone();
            String identity = tokenResult.getIdentity();

            String tokenKey = RedisPrefixUtils.generatorTokenKey(phone, identity, TokenTypeConstants.ACCESS_TOKEN_TYPE);
            //从redis缓存中取token
            String tokenRedis = stringRedisTemplate.opsForValue().get(tokenKey);
            //判断获取的token和redis中的token是否一致
            if((StringUtils.isBlank(tokenRedis)) || (!token.trim().equals(tokenRedis.trim()))){
                resultString = "access token invalid";
                result = false;
            }
        }

        if(!result){
            PrintWriter out = response.getWriter();
            out.print(JSONObject.fromObject(ResponseResult.fail(resultString)).toString());
        }

        return result;
    }
}
