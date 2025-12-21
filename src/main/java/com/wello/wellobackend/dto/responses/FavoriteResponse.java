package com.wello.wellobackend.dto.responses;

import com.wello.wellobackend.enums.MealType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteResponse {
    private int id;
    private String foodName;
    private int calories;
    private double protein;
    private double carbs;
    private double fat;
    private MealType mealType;
}
