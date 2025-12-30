package com.wello.wellobackend.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionSummary {
    private int totalCalories;
    private double totalProtein;
    private double totalCarbs;
    private double totalFat;
}
