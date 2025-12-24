package com.wello.wellobackend.dto.responses;

import com.wello.wellobackend.enums.MealType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteComboResponse {
    private int id;
    private String favoriteName;
    private MealType mealType;
    private List<FoodItemDetail> items;
    private NutritionSummary totalNutrition;
    private LocalDateTime createdAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FoodItemDetail {
        private int itemId;
        private int foodId;
        private String foodName;
        private int amountGrams;
        private int calories;
        private double protein;
        private double carbs;
        private double fat;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class NutritionSummary {
        private int totalCalories;
        private double totalProtein;
        private double totalCarbs;
        private double totalFat;
    }
}
