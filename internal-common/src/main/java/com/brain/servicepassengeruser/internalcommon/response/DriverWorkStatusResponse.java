package com.brain.servicepassengeruser.internalcommon.response;

import lombok.Data;

@Data
public class DriverWorkStatusResponse {
    private Long carId;

    private String driverPhone;

    private Long driverId;
}
