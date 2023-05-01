package com.brain.servicepassengeruser.internalcommon.dto;

import lombok.Data;


@Data
public class PriceRule {

    private String cityCode;

    private String vehicleType;

    private Double startFare;

    private Integer startMile;

    private Double unitPricePerMile;

    private Double unitPricePerMinute;
    //运价版本
    private Integer fareVersion;
    //运价类型
    private String fareType;

}
