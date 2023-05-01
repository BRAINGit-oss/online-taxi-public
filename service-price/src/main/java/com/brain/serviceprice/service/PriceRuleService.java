package com.brain.serviceprice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.brain.servicepassengeruser.internalcommon.constant.CommonStatusEnum;
import com.brain.servicepassengeruser.internalcommon.dto.PriceRule;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.serviceprice.mapper.PriceRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Brain
 * @since 2023-05-01
 */
@Service
public class PriceRuleService{

    @Autowired
    PriceRuleMapper priceRuleMapper;
    public ResponseResult add(PriceRule priceRule){

        //拼接fareType
        String cityCode = priceRule.getCityCode();
        String vehicleType = priceRule.getVehicleType();
        String fareType = cityCode+"$"+vehicleType;
        //添加版本号
        QueryWrapper<PriceRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("city_code",cityCode);
        queryWrapper.eq("vehicle_type",vehicleType);
        queryWrapper.orderByDesc("fare_version");

        //问题1：增加了版本号，前面的两个字段无法确定一条记录 2：找最大的版本号，需要排序
        List<PriceRule> list = priceRuleMapper.selectList(queryWrapper);
        Integer fareVesion = 0;
        if(list.size()>0){
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_EXISTS.getCode(),CommonStatusEnum.PRICE_RULE_EXISTS.getValue());
        }
        priceRule.setFareType(fareType);
        priceRule.setFareVersion(++fareVesion);

        priceRuleMapper.insert(priceRule);

        return ResponseResult.success("");
    }

    public ResponseResult edit(PriceRule priceRule){
        //拼接fareType
        String cityCode = priceRule.getCityCode();
        String vehicleType = priceRule.getVehicleType();
        String fareType = cityCode+"$"+vehicleType;
        priceRule.setFareType(fareType);
        //添加版本号
        QueryWrapper<PriceRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("city_code",cityCode);
        queryWrapper.eq("vehicle_type",vehicleType);
        queryWrapper.orderByDesc("fare_version");

        //问题1：增加了版本号，前面的两个字段无法确定一条记录 2：找最大的版本号，需要排序
        List<PriceRule> list = priceRuleMapper.selectList(queryWrapper);
        Integer fareVesion = 0;
        if(list.size()>0){
            PriceRule priceRule1 = list.get(0);
            Double unitPricePerMinute = priceRule1.getUnitPricePerMinute();
            Integer startMile = priceRule1.getStartMile();
            Double unitPricePerMile = priceRule1.getUnitPricePerMile();
            Double startFare = priceRule1.getStartFare();
            if(unitPricePerMile.doubleValue() == priceRule.getUnitPricePerMile()
            && startMile.intValue() == priceRule.getStartMile()
            && unitPricePerMinute.doubleValue() == priceRule.getUnitPricePerMinute()
            && startFare.doubleValue() == priceRule.getStartFare()){
                return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_NOT_EDIT.getCode(),CommonStatusEnum.PRICE_RULE_NOT_EDIT.getValue());
            }

            fareVesion = priceRule1.getFareVersion();
        }

        priceRule.setFareVersion(++fareVesion);

        priceRuleMapper.insert(priceRule);

        return ResponseResult.success("");
    }
}
