package com.wello.wellobackend.dto.responses;

import com.wello.wellobackend.enums.MealType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealLogResponse {
    private int id;
    private String foodName;
    private int amountGrams;
    private int calories;
    private int protein;
    private int carbs;
    private int fat;
    private MealType mealType;
    private LocalDateTime loggedAt;
}
