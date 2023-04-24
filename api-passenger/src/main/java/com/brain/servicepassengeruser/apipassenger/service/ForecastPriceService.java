package com.brain.servicepassengeruser.apipassenger.service;

import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.response.ForecastPriceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ForecastPriceService {

    public ResponseResult forecastPrice(String depLongitude,String depLatitude,String desLongitude,String desLatitude){

        log.info("出发地经度"+depLongitude);
        log.info("出发地纬度"+depLatitude);
        log.info("目的地经度"+desLongitude);
        log.info("目的地经度"+desLatitude);

        //根据出发点&目的地的经纬度计算价格 调用计价服务
        log.info("调用计价服务");

        ForecastPriceResponse forecastPriceResponse = new ForecastPriceResponse();
        forecastPriceResponse.setPrice(12.23);

        return ResponseResult.success(forecastPriceResponse);
    }
}
