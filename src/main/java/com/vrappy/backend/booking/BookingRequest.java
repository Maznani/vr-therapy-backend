package com.vrappy.backend.booking;

import lombok.Data;

@Data
public class BookingRequest {

    private Long therapistId;     // MUST be Long
    private String sessionDate;
    private String sessionTime;
    private int sessionDuration;
    private String sessionGoal;
    private String scenarioUsed;
}
