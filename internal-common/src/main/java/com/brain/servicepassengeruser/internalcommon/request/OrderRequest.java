package com.brain.servicepassengeruser.internalcommon.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderRequest {

    //乘客id
    private Long passengerId;
    //乘客phone
    private String passengerPhone;
    //发起地行政区划代码
    private String address;
    //预计用车时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime departTime;
    //订房发起时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderTime;
    //预计出发地点详细地址
    private String departure;
    //预计出发地点纬度
    private String depLatitude;
    //预计出发地点经度
    private String depLongitude;
    //预计目的地纬度
    private String destLatitude;
    //预计目的地经度
    private String destLongitude;
    //预计目的地
    private String destination;
    //坐标加密标识 1:GCJ-2 测绘局标准 2：WGS84 GPS标准 3：BD-09 百度标准 4：CGCS2000 北斗标准 0：其他
    private Integer encrypt;
    //运价类型编码
    private String fareType;
    //运价版本
    private Integer fareVersion;
    //设备名
    private String deviceCode;

}
