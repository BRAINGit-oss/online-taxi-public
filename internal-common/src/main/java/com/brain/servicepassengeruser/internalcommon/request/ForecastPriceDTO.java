package com.brain.servicepassengeruser.internalcommon.request;

import lombok.Data;

@Data
public class ForecastPriceDTO {
    private String depLongitude;

    private String depLatitude;

    private String desLongitude;

    private String desLatitude;
}
