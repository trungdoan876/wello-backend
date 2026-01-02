package com.wello.wellobackend.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/**
 * Response DTO cho sleep goal
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SleepGoalResponse {
    private Double sleepTargetHours;
    private LocalTime sleepBedtimeTarget;
    private LocalTime sleepWakeTimeTarget;
    private SleepRecommendation recommendation; // Khuyến nghị theo tuổi
}
