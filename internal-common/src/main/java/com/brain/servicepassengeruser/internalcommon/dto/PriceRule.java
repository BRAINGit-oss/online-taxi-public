package com.brain.servicepassengeruser.internalcommon.dto;

import lombok.Data;

@Data
public class PriceRule {

    private String cityCode;

    private String vehicleType;

    private String startFare;

    private int startMile;

    private double unitPricePerMile;

    private double unitPricePerMinute;
}
