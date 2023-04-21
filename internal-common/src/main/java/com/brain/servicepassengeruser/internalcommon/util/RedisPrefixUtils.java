package com.brain.servicepassengeruser.internalcommon.util;

public class RedisPrefixUtils {

    //乘客验证码的前缀
    public static String verificationCodePrefix = "passenger-verification-code-";

    //token的前缀
    public static String tokenPrefix = "token-";

    /**
     * 根据手机号，生成key
     * @param passengerPhone
     * @return
     */
    public static String generatorKeyByPhone(String passengerPhone){
        return verificationCodePrefix + passengerPhone;
    }

    /**
     * 根据手机号和身份表示生成token
     * @param phone
     * @param identity
     * @return
     */
    public static String generatorTokenKey(String phone,String identity){
        return tokenPrefix+phone+"-"+identity;
    }
}
