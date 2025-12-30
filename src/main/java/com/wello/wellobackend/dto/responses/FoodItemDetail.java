package com.wello.wellobackend.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodItemDetail {
    private int itemId;
    private int foodId;
    private String foodName;
    private int amountGrams;
    private int calories;
    private double protein;
    private double carbs;
    private double fat;
}
