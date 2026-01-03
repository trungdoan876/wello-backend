package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.responses.SevenDayStatsResponse;
import com.wello.wellobackend.model.*;
import com.wello.wellobackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class StatsServiceImpl implements StatsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WaterTrackerRepository waterTrackerRepository;

    @Autowired
    private WorkoutTrackerRepository workoutTrackerRepository;

    @Autowired
    private NutritionTrackerRepository nutritionTrackerRepository;

    @Autowired
    private TargetRepository targetRepository;

    @Override
    public SevenDayStatsResponse getSevenDayStats(int userId) {
        // Lấy user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Lấy target hiện tại để biết mục tiêu
        Target target = targetRepository.findByUser(user);
        if (target == null) {
            throw new RuntimeException("Target not found for user: " + userId);
        }

        // Tính toán ngày bắt đầu và kết thúc (7 ngày gần nhất)
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysAgo = today.minusDays(6); // 7 ngày bao gồm hôm nay

        LocalDateTime startDateTime = sevenDaysAgo.atStartOfDay();
        LocalDateTime endDateTime = today.atTime(LocalTime.MAX);

        // Khởi tạo counters & accumulators
        int daysWithSufficientWater = 0;
        int daysWithWorkout = 0;
        int daysExceedingCalories = 0;
        
        double totalWaterMl = 0;
        double maxWaterMl = 0;
        double minWaterMl = Double.MAX_VALUE;
        
        double totalCaloriesConsumed = 0;
        double totalCaloriesBurned = 0;
        double maxDailyCaloriesBurned = 0;
        
        int totalMeals = 0;
        int breakfastCount = 0;
        int lunchCount = 0;
        int dinnerCount = 0;
        int snackCount = 0;
        
        double totalCarbs = 0;
        double totalProtein = 0;
        double totalFat = 0;

        List<Double> dailyCaloriesList = new ArrayList<>();
        List<Double> dailyCaloriesBurnedList = new ArrayList<>();
        List<Double> dailyWaterList = new ArrayList<>();

        // Duyệt qua từng ngày trong 7 ngày
        for (LocalDate date = sevenDaysAgo; !date.isAfter(today); date = date.plusDays(1)) {
            LocalDateTime dayStart = date.atStartOfDay();
            LocalDateTime dayEnd = date.atTime(LocalTime.MAX);

            // === XỬ LÝ NƯỚC ===
            double dayWaterMl = getDailyWaterTotal(user, dayStart, dayEnd);
            dailyWaterList.add(dayWaterMl);
            totalWaterMl += dayWaterMl;
            maxWaterMl = Math.max(maxWaterMl, dayWaterMl);
            if (dayWaterMl > 0) {
                minWaterMl = Math.min(minWaterMl, dayWaterMl);
            }
            
            if (dayWaterMl >= target.getWaterIntakeMl()) {
                daysWithSufficientWater++;
            }

            // === XỬ LÝ NUTRITION (CALO & DINH DƯỠNG) ===
            List<NutritionTracker> dayNutritions = nutritionTrackerRepository.findByUserAndDateRange(user, dayStart, dayEnd);
            
            double dayCaloriesConsumed = 0;
            for (NutritionTracker nutrition : dayNutritions) {
                dayCaloriesConsumed += nutrition.getCaloriesConsumed();
                totalCarbs += nutrition.getCarbs();
                totalProtein += nutrition.getProtein();
                totalFat += nutrition.getFat();
                totalMeals++;
                
                // Đếm theo loại bữa ăn (kiểm tra null)
                if (nutrition.getMealType() != null) {
                    switch (nutrition.getMealType()) {
                        case BREAKFAST -> breakfastCount++;
                        case LUNCH -> lunchCount++;
                        case DINNER -> dinnerCount++;
                        case SNACK -> snackCount++;
                        default -> {}
                    }
                }
            }
            dailyCaloriesList.add(dayCaloriesConsumed);
            totalCaloriesConsumed += dayCaloriesConsumed;
            
            if (dayCaloriesConsumed > target.getCaloriesTarget()) {
                daysExceedingCalories++;
            }

            // === XỬ LÝ WORKOUT (CALO ĐỐT) ===
            List<WorkoutTracker> dayWorkouts = workoutTrackerRepository.findByUserAndDateRange(user, dayStart, dayEnd);
            double dayCaloriesBurned = 0;
            
            if (!dayWorkouts.isEmpty()) {
                daysWithWorkout++;
                for (WorkoutTracker workout : dayWorkouts) {
                    dayCaloriesBurned += workout.getTotalCaloriesBurned();
                }
            }
            
            dailyCaloriesBurnedList.add(dayCaloriesBurned);
            totalCaloriesBurned += dayCaloriesBurned;
            maxDailyCaloriesBurned = Math.max(maxDailyCaloriesBurned, dayCaloriesBurned);
        }

        // === TÍNH TRUNG BÌNH ===
        double averageWaterMl = totalWaterMl / 7;
        double averageCaloriesConsumed = totalCaloriesConsumed / 7;
        double averageCaloriesBurned = totalCaloriesBurned / 7;
        double totalCaloriesDeficit = totalCaloriesConsumed - totalCaloriesBurned;
        double averageCaloriesDeficit = totalCaloriesDeficit / 7;
        double averageMealsPerDay = (double) totalMeals / 7;
        double averageWorkoutDaysPerWeek = daysWithWorkout;
        double averageCarbsPerDay = totalCarbs / 7;
        double averageProteinPerDay = totalProtein / 7;
        double averageFatPerDay = totalFat / 7;

        // Xử lý min water (nếu không có ngày nào uống nước)
        if (minWaterMl == Double.MAX_VALUE) {
            minWaterMl = 0;
        }

        return SevenDayStatsResponse.builder()
                // === Cơ bản ===
                .daysWithSufficientWater(daysWithSufficientWater)
                .daysWithWorkout(daysWithWorkout)
                .daysExceedingCalories(daysExceedingCalories)
                .totalDays(7)
                
                // === Nước ===
                .totalWaterMl(totalWaterMl)
                .averageWaterMl(averageWaterMl)
                .maxWaterMl(maxWaterMl)
                .minWaterMl(minWaterMl)
                
                // === Calo ===
                .totalCaloriesConsumed(totalCaloriesConsumed)
                .totalCaloriesBurned(totalCaloriesBurned)
                .totalCaloriesDeficit(totalCaloriesDeficit)
                .averageCaloriesConsumed(averageCaloriesConsumed)
                .averageCaloriesBurned(averageCaloriesBurned)
                .averageCaloriesDeficit(averageCaloriesDeficit)
                
                // === Bữa ăn ===
                .totalMeals(totalMeals)
                .averageMealsPerDay(averageMealsPerDay)
                .breakfastCount(breakfastCount)
                .lunchCount(lunchCount)
                .dinnerCount(dinnerCount)
                .snackCount(snackCount)
                
                // === Tập luyện ===
                .workoutDays(daysWithWorkout)
                .averageWorkoutDaysPerWeek(averageWorkoutDaysPerWeek)
                .maxDailyCaloriesBurned(maxDailyCaloriesBurned)
                
                // === Dinh dưỡng ===
                .totalCarbs(totalCarbs)
                .totalProtein(totalProtein)
                .totalFat(totalFat)
                .averageCarbsPerDay(averageCarbsPerDay)
                .averageProteinPerDay(averageProteinPerDay)
                .averageFatPerDay(averageFatPerDay)
                
                // === Metadata ===
                .startDate(sevenDaysAgo.toString())
                .endDate(today.toString())
                .build();
    }

    /**
     * Lấy tổng lượng nước uống trong một ngày
     */
    private double getDailyWaterTotal(User user, LocalDateTime dayStart, LocalDateTime dayEnd) {
        List<WaterTracker> waterTrackers = waterTrackerRepository.findByUserAndDateRange(user, dayStart, dayEnd);
        return waterTrackers.stream()
                .mapToDouble(WaterTracker::getAmountMl)
                .sum();
    }

    /**
     * Kiểm tra xem trong một ngày có uống đủ nước không
     * Đủ nước = tổng ml nước uống >= mục tiêu hàng ngày
     */
    private boolean hasSufficientWaterIntake(User user, LocalDateTime dayStart, LocalDateTime dayEnd, Target target) {
        double totalWaterMl = getDailyWaterTotal(user, dayStart, dayEnd);
        return totalWaterMl >= target.getWaterIntakeMl();
    }

    /**
     * Kiểm tra xem trong một ngày có tập luyện không
     */
    private boolean hasWorkoutOnDay(User user, LocalDateTime dayStart, LocalDateTime dayEnd) {
        List<WorkoutTracker> workoutTrackers = workoutTrackerRepository.findByUserAndDateRange(user, dayStart, dayEnd);
        return !workoutTrackers.isEmpty();
    }

    /**
     * Kiểm tra xem trong một ngày có vượt quá mục tiêu calo không
     * Vượt = calo tiêu thụ > mục tiêu calo
     */
    private boolean hasExceededCalories(User user, LocalDateTime dayStart, LocalDateTime dayEnd, Target target) {
        List<NutritionTracker> nutritionTrackers = nutritionTrackerRepository.findByUserAndDateRange(user, dayStart, dayEnd);

        double totalCaloriesConsumed = nutritionTrackers.stream()
                .mapToDouble(NutritionTracker::getCaloriesConsumed)
                .sum();

        return totalCaloriesConsumed > target.getCaloriesTarget();
    }
}
