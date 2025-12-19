package com.wello.wellobackend.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutCalculationResponse {
    private int caloriesBurned;
    private double metValue;
    private double userWeight;
    private int durationMinutes;
}
