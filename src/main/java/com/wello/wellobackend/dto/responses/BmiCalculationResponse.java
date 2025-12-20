package com.wello.wellobackend.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BmiCalculationResponse {
    private double bmi;
    private String status; // UNDERWEIGHT, NORMAL, OVERWEIGHT, OBESE
    private String statusText; // Tiếng Việt
    private String warning; // Cảnh báo nếu có (null nếu không có)
}
