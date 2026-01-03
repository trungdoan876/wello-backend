package com.wello.wellobackend.dto.responses;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SevenDayStatsResponse {
    // === Thống kê cơ bản (ngày) ===
    private int daysWithSufficientWater;  // Số ngày uống đủ nước
    private int daysWithWorkout;          // Số ngày tập luyện
    private int daysExceedingCalories;    // Số ngày vượt calo
    private int totalDays;                // Tổng số ngày (7)

    // === Thống kê nước (7 ngày) ===
    private double totalWaterMl;          // Tổng lượng nước (ml)
    private double averageWaterMl;        // Trung bình nước/ngày (ml)
    private double maxWaterMl;            // Lượng nước nhiều nhất trong ngày (ml)
    private double minWaterMl;            // Lượng nước ít nhất trong ngày (ml)

    // === Thống kê calo (7 ngày) ===
    private double totalCaloriesConsumed; // Tổng calo tiêu thụ
    private double totalCaloriesBurned;   // Tổng calo tập luyện đốt
    private double totalCaloriesDeficit;  // Tổng calo dư ra (consumed - burned)
    private double averageCaloriesConsumed; // Trung bình calo/ngày
    private double averageCaloriesBurned; // Trung bình calo đốt/ngày
    private double averageCaloriesDeficit; // Trung bình calo dư/ngày

    // === Thống kê bữa ăn (7 ngày) ===
    private int totalMeals;               // Tổng số bữa ăn
    private double averageMealsPerDay;    // Trung bình bữa ăn/ngày
    private int breakfastCount;           // Số bữa sáng
    private int lunchCount;               // Số bữa trưa
    private int dinnerCount;              // Số bữa tối
    private int snackCount;               // Số bữa ăn vặt

    // === Thống kê tập luyện (7 ngày) ===
    private int workoutDays;              // Số ngày có tập luyện
    private double averageWorkoutDaysPerWeek; // Trung bình tập luyện/tuần
    private double maxDailyCaloriesBurned; // Calo đốt nhiều nhất trong ngày

    // === Thống kê dinh dưỡng (7 ngày) ===
    private double totalCarbs;            // Tổng carbs (g)
    private double totalProtein;          // Tổng protein (g)
    private double totalFat;              // Tổng fat (g)
    private double averageCarbsPerDay;    // Trung bình carbs/ngày (g)
    private double averageProteinPerDay;  // Trung bình protein/ngày (g)
    private double averageFatPerDay;      // Trung bình fat/ngày (g)

    // === Metadata ===
    private String startDate;             // Ngày bắt đầu thống kê
    private String endDate;               // Ngày kết thúc thống kê
}
