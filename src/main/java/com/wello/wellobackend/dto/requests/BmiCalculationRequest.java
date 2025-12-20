package com.wello.wellobackend.dto.requests;

import com.wello.wellobackend.enums.Goal;
import lombok.Data;

@Data
public class BmiCalculationRequest {
    private int weight; // kg
    private int height; // cm
    private Goal goal; // Optional - for warning calculation
}
