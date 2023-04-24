package com.brain.servicepassengeruser.internalcommon.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.brain.servicepassengeruser.internalcommon.dto.TokenResult;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    //security 眼
    private static final String SIGN = "CPFmsb!@#$$";

    private static final String JWT_KEY_PHONE = "phone";

    //乘客是1，用户是2
    private static final String JWT_KEY_IDENTITY = "identity";

    private static final String TOKEN_TYPE = "accessToken";

    private static final String TOKEN_TYPE_TIME = "tokenTime";

    //生成token
    public static String generatorToken(String passengerPhone,String identity,String accessToken){
        Map<String,String> map = new HashMap<>();
        map.put(JWT_KEY_PHONE,passengerPhone);
        map.put(JWT_KEY_IDENTITY,identity);
        map.put(TOKEN_TYPE,accessToken);
        //防止生成的token一致
        map.put(TOKEN_TYPE_TIME,Calendar.getInstance().getTime().toString());

        JWTCreator.Builder builder = JWT.create();

        //把map<k,v>值逐渐迭代到builder中 整合map
        map.forEach(
                (k,v) -> {
                    builder.withClaim(k,v);
            }
        );
        //整合过期时间 过期时间由redis设置
//        builder.withExpiresAt(date);

        //生成token
        String sign = builder.sign(Algorithm.HMAC256(SIGN));

        return sign;
    }

    //解析token
    public static TokenResult parseToken(String token){
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
        String phone = verify.getClaim(JWT_KEY_PHONE).asString();
        String identity = verify.getClaim(JWT_KEY_IDENTITY).asString();

        TokenResult tokenResult = new TokenResult();
        tokenResult.setPhone(phone);
        tokenResult.setIdentity(identity);
        return tokenResult;
    }

    public static TokenResult tokenCheck(String token){
        TokenResult tokenResult = null;
        try{
            tokenResult = JwtUtils.parseToken(token);
        }catch (Exception e){

        }
        return tokenResult;
    }

    @Test
    public void test(){
        String s = generatorToken("13253425342","1","accessToken");
        System.out.println("生成的token:"+s);
        System.out.println("解析后的token值：");
        TokenResult tokenResult = parseToken(s);
        System.out.println("手机号："+tokenResult.getPhone());
        System.out.println("身份："+tokenResult.getIdentity());
    }
}

