package com.brain.serviceprice.service;

import com.brain.servicepassengeruser.internalcommon.constant.CommonStatusEnum;
import com.brain.servicepassengeruser.internalcommon.dto.PriceRule;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.ForecastPriceDTO;
import com.brain.servicepassengeruser.internalcommon.response.DirectionResponse;
import com.brain.servicepassengeruser.internalcommon.response.ForecastPriceResponse;
import com.brain.servicepassengeruser.internalcommon.util.BigDecimalUtils;
import com.brain.serviceprice.mapper.PriceRuleMapper;
import com.brain.serviceprice.remote.ServiceMapClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ForecastPriceService {

    @Autowired
    ServiceMapClient serviceMapClient;

    @Autowired
    PriceRuleMapper priceRuleMapper;

    public ResponseResult forecastPriceByJW(String depLongitude,String depLatitude,String desLongitude,String desLatitude){
        log.info("出发地经度"+depLongitude);
        log.info("出发地纬度"+depLatitude);
        log.info("目的地经度"+desLongitude);
        log.info("目的地经度"+desLatitude);

        //根据出发点&目的地的经纬度计算价格 调用计价服务
        log.info("调用地图服务，查询距离和时长");
        ForecastPriceDTO forecastPriceDTO = new ForecastPriceDTO();
        forecastPriceDTO.setDepLongitude(depLongitude);
        forecastPriceDTO.setDepLatitude(depLatitude);
        forecastPriceDTO.setDesLongitude(desLongitude);
        forecastPriceDTO.setDesLatitude(desLatitude);
        ResponseResult<DirectionResponse> direction = serviceMapClient.direction(forecastPriceDTO);
        //计价素材
        Integer distance = direction.getData().getDistance();
        Integer duration = direction.getData().getDuration();
        log.info("距离：（米）"+distance);
        log.info("时长：（分）"+duration);

        log.info("读取计价规则");
        Map<String, Object> map = new HashMap<>();
        map.put("city_code","110000");
        map.put("vehicle_type","1");
        List<PriceRule> priceRuleMappers = priceRuleMapper.selectByMap(map);
        if(priceRuleMappers.size()==0){
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_ERROR.getCode(),CommonStatusEnum.PRICE_RULE_ERROR.getValue());
        }

        //计价规则
        PriceRule priceRule = priceRuleMappers.get(0);

        log.info("根据距离、时长和计价规则，计算价格");
        double price = getPrice(distance, duration, priceRule);

        ForecastPriceResponse forecastPriceResponse = new ForecastPriceResponse();
        forecastPriceResponse.setPrice(price);

        return ResponseResult.success(forecastPriceResponse);
    }

    /**
     * 根据距离、时长 和计价规则，计算最终价格
     * @param distance 距离：米
     * @param duration 时长：分
     * @param priceRule 计价规则
     * @return
     */
    private static double getPrice(Integer distance,Integer duration,PriceRule priceRule){
        //BigDecimal
        //起步价+（距离/1000-起步里程）*每里程单价+(秒/60)*每分钟单价
        //起步价
        double price = 0;
        double startFare = priceRule.getStartFare().doubleValue();
        price = BigDecimalUtils.add(price,startFare);

        //里程费 （距离/1000-起步里程）*每里程单价
        //里程价格
        double distanceDecimalDivide =  BigDecimalUtils.divide(distance,1000);
        //起步里程
        Integer startMile = priceRule.getStartMile();
        double distanceDecimalDivideSubtract=BigDecimalUtils.substract(distanceDecimalDivide,startMile);

        //每里程单价
        Double mile = distanceDecimalDivideSubtract<0?0:distanceDecimalDivideSubtract;
        Double unitPricePerMile = priceRule.getUnitPricePerMile();

        double mileFare = BigDecimalUtils.multiply(mile,unitPricePerMile);
        //里程价格
        price = BigDecimalUtils.add(startFare,mileFare);

        //时长费
        double minute = BigDecimalUtils.divide(duration,60);

        double unitPricePerMinute = priceRule.getUnitPricePerMinute();
        double timeFare = BigDecimalUtils.multiply(minute,unitPricePerMinute);

        price = BigDecimalUtils.add(price,timeFare);

        BigDecimal bigDecimal = new BigDecimal(price);
        BigDecimal priceDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_UP);

        return priceDecimal.doubleValue();
    }

    public static void main(String[] args) {
        PriceRule priceRule = new PriceRule();
        priceRule.setStartFare(10.0);
        priceRule.setStartMile(3);
        priceRule.setUnitPricePerMile(1.8);
        priceRule.setUnitPricePerMinute(0.5);

        System.out.println(getPrice(6500,1800,priceRule));
    }
}
