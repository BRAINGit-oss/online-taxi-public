package com.brain.servicepassengeruser.internalcommon.response;

import lombok.Data;

@Data
public class DirectionResponse {
    /**
     * 距离：米
     */
    private Integer distance;

    /**
     * 时长：分
     */
    private Integer duration;
}
