package com.wello.wellobackend.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyWorkoutSummaryResponse {
    private int totalCaloriesBurned;
    private List<WorkoutDetailResponse> workouts;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WorkoutDetailResponse {
        private int id;
        private String exerciseName;
        private int durationMinutes;
        private int caloriesBurned;
    }
}
