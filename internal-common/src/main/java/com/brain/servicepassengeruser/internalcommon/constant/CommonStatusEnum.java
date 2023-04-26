package com.brain.servicepassengeruser.internalcommon.constant;

import lombok.Data;
import lombok.Getter;


public enum CommonStatusEnum {

    /**
     * 枚举类型无参构造器 VERIFICATION_CODE_ERROR() 等价于 VERIFICATION_CODE_ERROR
     * 验证码错误提示：1000-1099
     */
    VERIFICATION_CODE_ERROR(1099,"验证码不正确"),

    /**
     * token提示：1100-1199
     */
    TOKEN_ERROR(1199,"token异常"),

    /**
     * user提示：1200-1299
     */
    USER_NOT_EXITS(1200,"用户不存在"),

    /**
     * 根据city_code&vehicle_type获取价格规则
     * 价格规则无效：1300-1399
     */
    PRICE_RULE_ERROR(1300,"价格规则无效"),

    /**
     * 行政区域无效：1400-1499
     */
    DISTRICT_ERROR(1400,"行政区域无效"),

    /**
     * 成功
     */
    SUCCESS(1,"success"),
    /**
     * 失败
     */
    FAIL(0,"fail");
    @Getter
    private int code;
    @Getter
    private String value;
    CommonStatusEnum(int code,String value){
        this.code = code;
        this.value = value;
    }
}
