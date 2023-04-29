package com.brain.servicepassengeruser.internalcommon.request;

import lombok.Data;

@Data
public class PointsRequest {
    private String tid;

    private String trid;

    private PointsDTO[] points;
}
