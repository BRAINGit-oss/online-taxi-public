package com.brain.servicepassengeruser.internalcommon.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author Brain
 * @since 2023-04-27
 */
@Data
public class DriverUserWorkStatus implements Serializable {
    private Long id;

    /**
     * 司机ID
     */
    private Long driverId;

    /**
     * 工作状态（0：收车，1：开始接单，2：暂停接单）
     */
    private Integer workStatus;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    private LocalDateTime gmtModified;
}
