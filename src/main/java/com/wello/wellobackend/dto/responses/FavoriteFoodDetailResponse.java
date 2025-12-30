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
public class FavoriteFoodDetailResponse {
    private int id;
    private String favoriteName;
    private MealType mealType;
    private List<FoodItemDetail> items;
    private NutritionSummary totalNutrition;
    private LocalDateTime createdAt;
}
