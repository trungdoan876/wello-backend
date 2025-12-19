package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.responses.DailyNutritionResponse;
import com.wello.wellobackend.dto.responses.WeeklyOverviewResponse;

import java.time.LocalDate;

public interface NutritionTrackerService {
    DailyNutritionResponse getDailySummary(int userId, LocalDate date);

    WeeklyOverviewResponse getWeeklyOverview(int userId, LocalDate startDate);

    com.wello.wellobackend.dto.responses.LogFoodResponse logFood(
            com.wello.wellobackend.dto.requests.LogFoodRequest request);
}
