package com.wello.wellobackend.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutLogResponse {
    private int workoutId;
    private String exerciseName;
    private int durationMinutes;
    private int caloriesBurned;
    private String message;
}
