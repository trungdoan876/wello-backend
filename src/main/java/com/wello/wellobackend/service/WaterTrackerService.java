package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.responses.DailyNutritionResponse;
import com.wello.wellobackend.dto.requests.AddWaterIntakeRequest;
import java.time.LocalDate;

public interface WaterTrackerService {
    DailyNutritionResponse.WaterIntake getDailyWaterIntake(int userId, LocalDate date);

    void addWaterIntake(AddWaterIntakeRequest request);

    void deleteWaterIntake(AddWaterIntakeRequest request);

    void deleteWaterIntakeById(int waterTrackerId);
}
