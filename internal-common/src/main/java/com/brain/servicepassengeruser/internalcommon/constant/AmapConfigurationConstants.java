package com.brain.servicepassengeruser.internalcommon.constant;

public class AmapConfigurationConstants {
    /**
     * 路径规划地址
     */
    public static final String DIRERECTION_URL = "https://restapi.amap.com/v3/direction/driving";

    /**
     * 行政区域编码地址
     */
    public static final String DISTRICT_URL = "https://restapi.amap.com/v3/config/district";

    /**
     * 服务创建地址
     */
    public static final String SERVICE_ADD = "https://tsapi.amap.com/v1/track/service/add";

    /**
     * 终端创建地址
     */
    public static final String TERMINAL_ADD = "https://tsapi.amap.com/v1/track/terminal/add";

    /**
     * 轨迹创建地址
     */
    public static final String TRACK_ADD = "https://tsapi.amap.com/v1/track/trace/add";

    /**
     * 轨迹上传地址
     */
    public static final String POINTS_UPLOAD = "https://tsapi.amap.com/v1/track/point/upload";

    /**
     * 状态 1：成功；0：失败
     */
    public static final String STATUS = "status";

    /**
     * 路径选择
     */
    public static final String ROUTE = "route";

    /**
     * 具体路径
     */
    public static final String PATHS = "paths";

    /**
     * 距离：米（int）
     */
    public static final String DISTANCE = "distance";

    /**
     * 时长：分（int）
     */
    public static final String DURATION = "duration";

    public static final String ADCODE = "adcode";

    public static final String NAME = "name";

    public static final String LEVEL = "level";

    public static final String DISTRICTS = "districts";

    public static final String STREET = "street";

    /**
     * 服务创建响应data
     */
    public static final String DATA = "data";

    /**
     * 服务创建响应sid
     */
    public static final String SID = "sid";

    /**
     * 终端创建响应tid
     */
    public static final String TID = "tid";

    /**
     * 轨迹创建响应trid
     */
    public static final String TRID = "trid";
}
