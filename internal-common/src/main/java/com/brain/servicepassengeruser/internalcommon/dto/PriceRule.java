package com.brain.servicepassengeruser.internalcommon.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceRule {

    private String cityCode;

    private String vehicleType;

    private BigDecimal startFare;

    private Integer startMile;

    private Double unitPricePerMile;

    private Double unitPricePerMinute;
}
