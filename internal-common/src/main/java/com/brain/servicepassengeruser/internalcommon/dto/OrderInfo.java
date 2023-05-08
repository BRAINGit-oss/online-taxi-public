package com.brain.servicepassengeruser.internalcommon.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author Brain
 * @since 2023-05-01
 */
@Data
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表主键id
     */
    private Long id;

    /**
     * 乘客id
     */
    private Long passengerId;

    /**
     * 乘客phone
     */
    private String passengerPhone;

    /**
     * 司机id
     */
    private Long driverId;

    /**
     * 司机phone
     */
    private String driverPhone;

    /**
     * 车辆id
     */
    private Long carId;

    /**
     * 发起地行政区划代码
     */
    private String address;

    /**
     * 订单发起时间
     */
    private LocalDateTime orderTime;

    /**
     * 预计用车时间
     */
    private LocalDateTime departTime;

    /**
     * 预计出发地点详细地址
     */
    private String departure;

    /**
     * 预计出发地点经度
     */
    private String depLongitude;

    /**
     * 预计触发地点纬度
     */
    private String depLatitude;

    /**
     * 预计目的地
     */
    private String destination;

    /**
     * 预计目的地经度
     */
    private String destLongitude;

    /**
     * 预计目的地纬度
     */
    private String destLatitude;

    /**
     * 坐标加密标识 1:GCJ-2 测绘局标准 2：WGS84 GPS标准 3：BD-09 百度标准 4：CGCS2000 北斗标准 0：其他
     */
    private Integer encrypt;

    /**
     * 运价类型编码
     */
    private String fareType;

    /**
     * 运费类型
     */
    private Integer fareVersion;

    /**
     * 接车时车辆经度
     */
    private String receiveOrderCarLongitude;

    /**
     * 接车时车辆纬度
     */
    private String receiveOrderCarLatitude;

    /**
     * 接车订单时间
     */
    private LocalDate receiveOrderTime;

    /**
     * 机动车驾驶证编号
     */
    private String licenseId;

    /**
     * 车辆编号
     */
    private String vehicleNo;

    /**
     * 接单时间
     */
    private LocalDateTime toPickUpPassengerTime;

    /**
     * 接单乘客经度
     */
    private String toPickUpPassengerLongitude;

    /**
     * 接单乘客纬度
     */
    private String toPickUpPassengerLatitude;

    /**
     * 乘客出发地
     */
    private String toPickUpPassengerAddress;

    /**
     * 司机到达目的地时间
     */
    private LocalDateTime driverArrivedDepartureTime;

    /**
     * 上车时间
     */
    private LocalDateTime pickUpPassengerTime;

    /**
     * 上车经度
     */
    private String pickUpPassengerLongitude;

    /**
     * 上车纬度
     */
    private String pickUpPassengerLatitude;

    /**
     * 司机送达时间
     */
    private LocalDateTime passengerGetoffTime;

    /**
     * 司机送达经度
     */
    private String passengerGetoffLongitude;

    /**
     * 司机送达纬度
     */
    private String passengerGetoffLatitude;

    /**
     * 订单取消时间
     */
    private LocalDateTime cancelTime;

    /**
     * 订单取消操作
     */
    private Integer cancelOperator;

    /**
     * 订单取消类型
     */
    private Integer cancelTypeCode;

    /**
     * 行程公里数
     */
    private Long driverMile;

    /**
     * 行程时长
     */
    private Long driverTime;

    /**
     * 订单状态 	1：订单开始  2：司机接单 3：去接乘客 4：司机到达乘客起点 5：乘客上车，司机开始行程 6：到达目的地，行程结束，未支付 7：发起收款 8：支付完成 9：订单取消
     */
    private Integer orderStatus;

    /**
     * 订单创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 订单修改时间
     */
    private LocalDateTime gmtModified;

}
