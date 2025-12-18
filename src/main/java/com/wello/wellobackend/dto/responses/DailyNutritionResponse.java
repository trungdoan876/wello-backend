package com.wello.wellobackend.dto.responses;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyNutritionResponse {
    private LocalDate date;
    private int caloriesConsumed;
    private int caloriesBurned;
    private int caloriesRemaining;
    private Macros macros;
    private WaterIntake waterIntake;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Macros {
        private MacroDetail carb;
        private MacroDetail protein;
        private MacroDetail fat;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MacroDetail {
        private int consumed;
        private int target;
        private String unit;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WaterIntake {
        private int consumed;
        private int target;
        private String unit;
        private int glasses;
    }
}
