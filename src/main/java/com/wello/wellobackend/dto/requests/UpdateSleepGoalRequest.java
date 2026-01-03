package com.wello.wellobackend.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSleepGoalRequest {
    private Double sleepTargetHours;
    private LocalTime sleepBedtimeTarget;
    private LocalTime sleepWakeTimeTarget;
}
