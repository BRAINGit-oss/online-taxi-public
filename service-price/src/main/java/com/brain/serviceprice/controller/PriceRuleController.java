package com.brain.serviceprice.controller;


import com.brain.servicepassengeruser.internalcommon.dto.PriceRule;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.serviceprice.service.PriceRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Brain
 * @since 2023-05-01
 */
@RestController
@RequestMapping("/price-rule")
public class PriceRuleController {

    @Autowired
    PriceRuleService priceRuleService;

    @PostMapping("/add")
    public ResponseResult add(@RequestBody PriceRule priceRule){

        return priceRuleService.add(priceRule);
    }

    @PostMapping("/edit")
    public ResponseResult edit(@RequestBody PriceRule priceRule){

        return priceRuleService.edit(priceRule);
    }

    @GetMapping("/get-latest-version")
    public ResponseResult<PriceRule> getLatestVersion(@RequestParam String fareType){

        return priceRuleService.getLatestVersion(fareType);
    }

    @GetMapping("/is_latest")
    public ResponseResult<Boolean> isLatast(@RequestParam String fareType,@RequestParam Integer fareVersion){

        return priceRuleService.isLatest(fareType,fareVersion);
    }
}
