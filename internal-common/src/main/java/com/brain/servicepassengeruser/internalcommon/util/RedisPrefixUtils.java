package com.brain.servicepassengeruser.internalcommon.util;

public class RedisPrefixUtils {

    //乘客验证码的前缀
    public static String verificationCodePrefix = "verification-code-";

    //token的前缀
    public static String tokenPrefix = "token-";

    //设备的前缀
    public static String devicePrefix = "black-device-";

    /**
     * 根据手机号，生成key
     * @param passengerPhone
     * @return
     */
    /**
     *
     * @param passengerPhone
     * @param instance 身份标识 1：乘客 2：司机
     * @return
     */
    public static String generatorKeyByPhone(String passengerPhone,String instance){
        return verificationCodePrefix+ instance+"-" + passengerPhone;
    }

    /**
     * 根据手机号和身份表示生成token
     * @param phone
     * @param identity
     * @return
     */
    public static String generatorTokenKey(String phone,String identity,String accessToken){
        return tokenPrefix+phone+"-"+identity+"-"+accessToken;
    }

    public static String generatorKeyByDevice(String deviceCode){
        return devicePrefix+ deviceCode;
    }

    public static String generatorKeyByIdAndIdentity(Long userId,String identity){
        return userId+"-"+identity+"-";
    }
}
