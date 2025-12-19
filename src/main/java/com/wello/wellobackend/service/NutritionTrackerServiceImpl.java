package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.responses.DailyNutritionResponse;
import com.wello.wellobackend.dto.responses.WeeklyOverviewResponse;
import com.wello.wellobackend.model.Food;
import com.wello.wellobackend.model.NutritionTracker;
import com.wello.wellobackend.model.Target;
import com.wello.wellobackend.model.User;
import com.wello.wellobackend.model.WaterTracker;
import com.wello.wellobackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NutritionTrackerServiceImpl implements NutritionTrackerService {

        @Autowired
        private AuthRepository authRepository;

        @Autowired
        private NutritionTrackerRepository nutritionTrackerRepository;

        @Autowired
        private TargetRepository targetRepository;

        @Autowired
        private WaterTrackerRepository waterTrackerRepository;

        @Autowired
        private FoodRepository foodRepository;

        @Override
        public DailyNutritionResponse getDailySummary(int userId, LocalDate date) {
                User user = authRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                LocalDateTime startOfDay = LocalDateTime.of(date, LocalTime.MIN);
                LocalDateTime endOfDay = LocalDateTime.of(date, LocalTime.MAX);

                NutritionTracker nutrition = nutritionTrackerRepository.findByUserAndDate(user, startOfDay, endOfDay)
                                .orElse(new NutritionTracker());

                WaterTracker water = waterTrackerRepository.findByUserAndDate(user, startOfDay, endOfDay)
                                .orElse(new WaterTracker());

                Target target = targetRepository.findByUser(user);
                int targetCals = target != null ? target.getCaloriesTarget() : 0;

                return DailyNutritionResponse.builder()
                                .date(date)
                                .caloriesConsumed(nutrition.getCaloriesConsumed())
                                .caloriesBurned(nutrition.getCaloriesBurned())
                                .caloriesRemaining(
                                                Math.max(0, targetCals - nutrition.getCaloriesConsumed()))
                                .macros(DailyNutritionResponse.Macros.builder()
                                                .carb(DailyNutritionResponse.MacroDetail.builder()
                                                                .consumed(nutrition.getCarbs())
                                                                .target(target != null ? target.getCarbTarget() : 0)
                                                                .unit("g")
                                                                .build())
                                                .protein(DailyNutritionResponse.MacroDetail.builder()
                                                                .consumed(nutrition.getProtein())
                                                                .target(target != null ? target.getProteinTarget() : 0)
                                                                .unit("g")
                                                                .build())
                                                .fat(DailyNutritionResponse.MacroDetail.builder()
                                                                .consumed(nutrition.getFat())
                                                                .target(target != null ? target.getFatTarget() : 0)
                                                                .unit("g")
                                                                .build())
                                                .build())
                                .waterIntake(DailyNutritionResponse.WaterIntake.builder()
                                                .consumed(water.getAmountMl())
                                                .target(target != null ? target.getWaterIntakeMl() : 0)
                                                .unit("ml")
                                                .glasses(water.getAmountMl() / 250) // Assuming 250ml per glass
                                                .build())
                                .build();
        }

        @Override
        public WeeklyOverviewResponse getWeeklyOverview(int userId, LocalDate startDate) {
                User user = authRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                LocalDateTime start = LocalDateTime.of(startDate, LocalTime.MIN);
                LocalDateTime end = LocalDateTime.of(startDate.plusDays(6), LocalTime.MAX);

                List<NutritionTracker> data = nutritionTrackerRepository.findByUserAndDateRange(user, start, end);
                Map<LocalDate, NutritionTracker> dataMap = data.stream()
                                .collect(Collectors.toMap(n -> n.getDate().toLocalDate(), n -> n,
                                                (existing, replacement) -> existing));

                List<WeeklyOverviewResponse.DayOverview> weekData = new ArrayList<>();
                for (int i = 0; i < 7; i++) {
                        LocalDate date = startDate.plusDays(i);
                        NutritionTracker n = dataMap.get(date);
                        weekData.add(WeeklyOverviewResponse.DayOverview.builder()
                                        .date(date)
                                        .dayOfWeek(getShortDayName(date))
                                        .caloriesConsumed(n != null ? n.getCaloriesConsumed() : 0)
                                        .hasData(n != null)
                                        .build());
                }

                return WeeklyOverviewResponse.builder()
                                .weekData(weekData)
                                .currentDate(LocalDate.now())
                                .build();
        }

        @Override
        @org.springframework.transaction.annotation.Transactional
        public com.wello.wellobackend.dto.responses.LogFoodResponse logFood(
                        com.wello.wellobackend.dto.requests.LogFoodRequest request) {
                User user = authRepository.findById(request.getUserId())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                Food food = foodRepository.findById(request.getFoodId())
                                .orElseThrow(() -> new RuntimeException("Food not found"));

                LocalDateTime startOfDay = LocalDateTime.of(request.getDate(), LocalTime.MIN);
                LocalDateTime endOfDay = LocalDateTime.of(request.getDate(), LocalTime.MAX);

                NutritionTracker tracker = nutritionTrackerRepository.findByUserAndDate(user, startOfDay, endOfDay)
                                .orElseGet(() -> {
                                        NutritionTracker newTracker = new NutritionTracker();
                                        newTracker.setUser(user);
                                        newTracker.setDate(LocalDateTime.of(request.getDate(), LocalTime.now()));
                                        return nutritionTrackerRepository.save(newTracker);
                                });

                // Calculate nutrients based on amount (per 100g)
                double factor = request.getAmountGrams() / 100.0;
                int calories = (int) (food.getCaloriesPer100g() * factor);
                double protein = food.getProteinPer100g() * factor;
                double carbs = food.getCarbsPer100g() * factor;
                double fat = food.getFatPer100g() * factor;

                // Update tracker totals
                tracker.setCaloriesConsumed(tracker.getCaloriesConsumed() + calories);
                tracker.setProtein(tracker.getProtein() + (int) Math.round(protein));
                tracker.setCarbs(tracker.getCarbs() + (int) Math.round(carbs));
                tracker.setFat(tracker.getFat() + (int) Math.round(fat));
                nutritionTrackerRepository.save(tracker);

                return com.wello.wellobackend.dto.responses.LogFoodResponse.builder()
                                .foodName(food.getFoodName())
                                .calories(calories)
                                .protein(protein)
                                .carbs(carbs)
                                .fat(fat)
                                .message("Food logged successfully")
                                .build();
        }

        private String getShortDayName(LocalDate date) {
                // Return first letter of day name in Vietnamese (simplified)
                // H, B, T, N, S, B, C (Monday to Sunday)
                String day = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("vi-VN"));
                return day.substring(0, 1).toUpperCase();
        }
}
