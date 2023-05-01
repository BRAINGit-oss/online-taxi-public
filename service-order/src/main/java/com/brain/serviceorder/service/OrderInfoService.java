package com.brain.serviceorder.service;

import com.brain.serviceorder.mapper.OrderInfoMapper;
import com.brain.servicepassengeruser.internalcommon.dto.OrderInfo;
import com.brain.servicepassengeruser.internalcommon.dto.ResponseResult;
import com.brain.servicepassengeruser.internalcommon.request.OrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Brain
 * @since 2023-05-01
 */
@Service
@Slf4j
public class OrderInfoService {
    @Autowired
    OrderInfoMapper orderInfoMapperl;
    public ResponseResult add(OrderRequest orderRequest){

        log.info(orderRequest.getAddress());
        OrderInfo orderInfo = new OrderInfo();
        //一次性复制请求信息
        BeanUtils.copyProperties(orderRequest,orderInfo);

        orderInfoMapperl.insert(orderInfo);


        return ResponseResult.success("");
    }

}
