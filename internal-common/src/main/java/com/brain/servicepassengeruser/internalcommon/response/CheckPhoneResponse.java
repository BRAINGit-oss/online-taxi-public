package com.brain.servicepassengeruser.internalcommon.response;

import lombok.Data;

@Data
public class CheckPhoneResponse {
    /**
     * 司机手机号
     */
    private String driverPhone;

    /**
     * 是否存在
     */
    private Integer ifEmpty;
}
