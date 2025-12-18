package com.wello.wellobackend.dto.responses;

import com.wello.wellobackend.enums.Goal;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse {
    private int userId;
    private double currentWeight;
    private double targetWeight;
    private Goal goalType;
    private int dailyCalorieTarget;
    private int dailyCalorieBurned;
    private int dailyWaterTarget;
    private MacroTargets macroTargets;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MacroTargets {
        private int carb;
        private int protein;
        private int fat;
    }
}
