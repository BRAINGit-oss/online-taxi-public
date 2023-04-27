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
 * @since 2023-04-26
 */
@Data
public class DriverCarBindingRelationship implements Serializable {

    private Long id;

    /**
     * 司机ID
     */
    private Long driverId;

    /**
     * 汽车ID
     */
    private Long carId;

    /**
     * 绑定状态（1：绑定，2：解绑）
     */
    private Integer state;

    /**
     * 绑定时间
     */
    private LocalDateTime bindingTime;

    /**
     * 解绑时间
     */
    private LocalDateTime unBindingTime;
}
