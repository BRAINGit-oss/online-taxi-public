package com.brain.servicepassengeruser.internalcommon.request;

import lombok.Data;

@Data
public class ForecastPriceDTO {
    /**
     * 出发地经度
     */
    private String depLongitude;

    /**
     * 出发地纬度
     */
    private String depLatitude;

    /**
     * 目的地经度
     */
    private String desLongitude;

    /**
     * 目的地纬度
     */
    private String desLatitude;

    /**
     * 城市行政区划代码
     */
    private String cityCode;

    /**
     * 车辆类型
     */
    private String vehicleType;
}
