package com.wello.wellobackend.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogFoodResponse {
    private String foodName;
    private int calories;
    private double protein;
    private double carbs;
    private double fat;
    private String message;
}
