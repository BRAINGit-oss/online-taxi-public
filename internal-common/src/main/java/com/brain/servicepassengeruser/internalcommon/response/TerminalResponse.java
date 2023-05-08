package com.brain.servicepassengeruser.internalcommon.response;

import lombok.Data;

@Data
public class TerminalResponse {
    /**
     * 终端创建ID
     */
    private String tid;

    private Long carId;

    private String longitude;
    private String latitude;
}
