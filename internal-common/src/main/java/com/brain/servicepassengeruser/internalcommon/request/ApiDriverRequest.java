package com.brain.servicepassengeruser.internalcommon.request;

import lombok.Data;

@Data
public class ApiDriverRequest {

    public Long carId;

    private PointsDTO[] points;

}
