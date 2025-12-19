package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.responses.DailyNutritionResponse;
import java.time.LocalDate;

public interface WaterTrackerService {
    DailyNutritionResponse.WaterIntake getDailyWaterIntake(int userId, LocalDate date);

    void addWaterIntake(com.wello.wellobackend.dto.requests.AddWaterIntakeRequest request);

    void deleteWaterIntake(com.wello.wellobackend.dto.requests.AddWaterIntakeRequest request);

    void deleteWaterIntakeById(int waterTrackerId);
}
