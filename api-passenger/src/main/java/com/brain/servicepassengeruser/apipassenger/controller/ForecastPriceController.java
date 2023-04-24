package com.brain.servicepassengeruser.apipassenger.controller;

import com.brain.servicepassengeruser.apipassenger.service.ForecastPriceService;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.ForecastPriceDTO;
import com.brain.servicepassengeruser.internalcommon.response.ForecastPriceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForecastPriceController {

    @Autowired
    ForecastPriceService forecastPriceService;

    @PostMapping("/forecast-price")
    public ResponseResult forecastPrice(@RequestBody  ForecastPriceDTO forecastPriceDTO){
        String depLongitude = forecastPriceDTO.getDepLongitude();
        String depLatitude = forecastPriceDTO.getDepLatitude();
        String desLongitude = forecastPriceDTO.getDesLongitude();
        String desLatitude = forecastPriceDTO.getDesLatitude();

        return forecastPriceService.forecastPrice(depLongitude,depLatitude,desLongitude,desLatitude);
    }
}
