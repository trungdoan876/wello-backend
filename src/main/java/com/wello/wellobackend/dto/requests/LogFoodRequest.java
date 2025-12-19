package com.wello.wellobackend.dto.requests;

import com.wello.wellobackend.enums.MealType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogFoodRequest {
    private int userId;
    private int foodId;
    private int amountGrams;
    private LocalDate date;
    private MealType mealType;
}
