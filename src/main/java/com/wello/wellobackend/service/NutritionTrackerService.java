package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.requests.LogFoodRequest;
import com.wello.wellobackend.dto.responses.DailyNutritionResponse;
import com.wello.wellobackend.dto.responses.LogFoodResponse;
import com.wello.wellobackend.dto.responses.MealLogResponse;
import com.wello.wellobackend.dto.responses.WeeklyOverviewResponse;

import java.time.LocalDate;
import java.util.List;

public interface NutritionTrackerService {
        DailyNutritionResponse getDailySummary(int userId, LocalDate date);

        WeeklyOverviewResponse getWeeklyOverview(int userId, LocalDate startDate);

        LogFoodResponse logFood(LogFoodRequest request);

        List<MealLogResponse> getDailyMealHistory(int userId, LocalDate date);
}
