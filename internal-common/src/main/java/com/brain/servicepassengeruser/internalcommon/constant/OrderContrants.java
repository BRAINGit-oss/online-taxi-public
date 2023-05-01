package com.brain.servicepassengeruser.internalcommon.constant;

import lombok.Data;

@Data
public class OrderContrants {

    /**
     * 订单状态
     * 1：订单开始
     */
    private Integer ORDER_START = 1;
//    2：司机接单
    private Integer RECEIVE_ORDER = 2;
//    3：去接乘客
    private Integer DRIVER_TO_PICK_UP_PASSENGER =3;
//    4：司机到达乘客起点
    private Integer DRIVER_ADDRIVED_DEPATURE = 4;
//    5：乘客上车，司机开始行程
    private Integer PICK_UP_PASSENGER = 5;
//    6：到达目的地，行程结束，未支付
    private Integer PASSENGER_GETOFF = 6;
//    7：发起收款
    private Integer TO_START_PAY = 7;
//    8：支付完成
    private Integer SUCCESS_PAY = 8;
//     9：订单取消
    private Integer ORDER_CANCEL = 9;
}
