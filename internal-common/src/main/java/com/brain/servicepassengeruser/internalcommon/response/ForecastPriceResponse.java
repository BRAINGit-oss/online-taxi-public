package com.brain.servicepassengeruser.internalcommon.response;

import lombok.Data;

@Data
public class ForecastPriceResponse {
    private double price;
    /**
     * 城市行政区划代码
     */
    private String cityCode;

    /**
     * 车辆类型
     */
    private String vehicleType;
}
