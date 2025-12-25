package com.wello.wellobackend.dto.requests;

import com.wello.wellobackend.enums.MealType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateFavoriteRequest {
    private int userId;
    private int favoriteId;
    private String foodName;
    private int caloriesPer100g;
    private double proteinPer100g;
    private double carbsPer100g;
    private double fatPer100g;
    private MealType mealType;
}
