package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.requests.LogFoodRequest;
import com.wello.wellobackend.dto.responses.DailyNutritionResponse;
import com.wello.wellobackend.dto.responses.LogFoodResponse;
import com.wello.wellobackend.dto.responses.FoodLogResponse;
import com.wello.wellobackend.dto.responses.WeeklyOverviewResponse;
import com.wello.wellobackend.model.*;
import com.wello.wellobackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.*;
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
                                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

                LocalDateTime startOfDay = LocalDateTime.of(date, LocalTime.MIN);
                LocalDateTime endOfDay = LocalDateTime.of(date, LocalTime.MAX);

                List<NutritionTracker> nutritionLogs = nutritionTrackerRepository.findByUserAndDateRange(user,
                                startOfDay, endOfDay);

                int totalCaloriesConsumed = nutritionLogs.stream().mapToInt(NutritionTracker::getCaloriesConsumed)
                                .sum();
                int totalCaloriesBurned = nutritionLogs.stream().mapToInt(NutritionTracker::getCaloriesBurned).sum();
                int totalCarbs = nutritionLogs.stream().mapToInt(NutritionTracker::getCarbs).sum();
                int totalProtein = nutritionLogs.stream().mapToInt(NutritionTracker::getProtein).sum();
                int totalFat = nutritionLogs.stream().mapToInt(NutritionTracker::getFat).sum();

                WaterTracker water = waterTrackerRepository.findByUserAndDate(user, startOfDay, endOfDay)
                                .orElse(new WaterTracker());

                Target target = targetRepository.findByUser(user);
                int targetCals = target != null ? target.getCaloriesTarget() : 0;

                return DailyNutritionResponse.builder()
                                .date(date)
                                .caloriesConsumed(totalCaloriesConsumed)
                                .caloriesBurned(totalCaloriesBurned)
                                .caloriesRemaining(
                                                Math.max(0, targetCals - totalCaloriesConsumed))
                                .macros(DailyNutritionResponse.Macros.builder()
                                                .carb(DailyNutritionResponse.MacroDetail.builder()
                                                                .consumed(totalCarbs)
                                                                .target(target != null ? target.getCarbTarget() : 0)
                                                                .unit("g")
                                                                .build())
                                                .protein(DailyNutritionResponse.MacroDetail.builder()
                                                                .consumed(totalProtein)
                                                                .target(target != null ? target.getProteinTarget() : 0)
                                                                .unit("g")
                                                                .build())
                                                .fat(DailyNutritionResponse.MacroDetail.builder()
                                                                .consumed(totalFat)
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
                                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

                LocalDateTime start = LocalDateTime.of(startDate, LocalTime.MIN);
                LocalDateTime end = LocalDateTime.of(startDate.plusDays(6), LocalTime.MAX);

                List<NutritionTracker> data = nutritionTrackerRepository.findByUserAndDateRange(user, start, end);

                // Group by date and sum calories
                Map<LocalDate, Integer> dailyCalories = data.stream()
                                .collect(Collectors.groupingBy(
                                                n -> n.getDate().toLocalDate(),
                                                Collectors.summingInt(NutritionTracker::getCaloriesConsumed)));

                List<WeeklyOverviewResponse.DayOverview> weekData = new ArrayList<>();
                for (int i = 0; i < 7; i++) {
                        LocalDate date = startDate.plusDays(i);
                        Integer calories = dailyCalories.getOrDefault(date, 0);
                        boolean hasData = dailyCalories.containsKey(date);
                        weekData.add(WeeklyOverviewResponse.DayOverview.builder()
                                        .date(date)
                                        .dayOfWeek(getShortDayName(date))
                                        .caloriesConsumed(calories)
                                        .hasData(hasData)
                                        .build());
                }

                return WeeklyOverviewResponse.builder()
                                .weekData(weekData)
                                .currentDate(LocalDate.now())
                                .build();
        }

        @Override
        @Transactional
        public LogFoodResponse logFood(LogFoodRequest request) {
                User user = authRepository.findById(request.getUserId())
                                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

                Food food = foodRepository.findById(request.getFoodId())
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy thực phẩm"));

                // Calculate nutrition based on amount
                double factor = request.getAmountGrams() / 100.0;

                int calories = (int) (food.getCaloriesPer100g() * factor);
                double protein = food.getProteinPer100g() * factor;
                double carbs = food.getCarbsPer100g() * factor;
                double fat = food.getFatPer100g() * factor;

                // Create new tracker entry for this specific food log
                NutritionTracker tracker = new NutritionTracker();
                tracker.setUser(user);
                tracker.setDate(LocalDateTime.of(request.getDate(), LocalTime.now()));
                tracker.setFood(food);
                tracker.setAmountGrams(request.getAmountGrams());
                tracker.setMealType(request.getMealType());
                tracker.setCaloriesConsumed(calories);
                tracker.setProtein((int) Math.round(protein));
                tracker.setCarbs((int) Math.round(carbs));
                tracker.setFat((int) Math.round(fat));
                tracker.setCaloriesBurned(0);

                nutritionTrackerRepository.save(tracker);

                return LogFoodResponse.builder()
                                .foodName(food.getFoodName())
                                .calories(calories)
                                .protein(protein)
                                .carbs(carbs)
                                .fat(fat)
                                .message("Food logged successfully to " + request.getMealType())
                                .build();
        }

        @Override
        public List<FoodLogResponse> getDailyFoodHistory(int userId, LocalDate date) {
                User user = authRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

                LocalDateTime startOfDay = LocalDateTime.of(date, LocalTime.MIN);
                LocalDateTime endOfDay = LocalDateTime.of(date, LocalTime.MAX);

                List<NutritionTracker> logs = nutritionTrackerRepository.findByUserAndDateRange(user,
                                startOfDay, endOfDay);

                return logs.stream()
                                .filter(log -> log.getFood() != null) // Only food logs, not workout logs
                                .map(log -> FoodLogResponse.builder()
                                                .id(log.getId())
                                                .foodName(log.getFood().getFoodName())
                                                .amountGrams(log.getAmountGrams())
                                                .calories(log.getCaloriesConsumed())
                                                .protein(log.getProtein())
                                                .carbs(log.getCarbs())
                                                .fat(log.getFat())
                                                .mealType(log.getMealType())
                                                .loggedAt(log.getDate())
                                                .favoriteName(log.getFavoriteName())
                                                .foodId(log.getFood().getId())
                                                .imageUrl(log.getFood().getImageUrl())
                                                .build())
                                .collect(Collectors.toList());
        }

        private String getShortDayName(LocalDate date) {
                // Return first letter of day name in Vietnamese (simplified)
                // H, B, T, N, S, B, C (Monday to Sunday)
                String day = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("vi-VN"));
                return day.substring(0, 1).toUpperCase();
        }
}
