package com.wello.wellobackend.service;

import com.wello.wellobackend.enums.Goal;

public interface BmiCalculationService {
    double calculateBMI(int weight, int height);

    String getBMIStatus(double bmi);

    String getBMIStatusText(String status);

    String getGoalWarning(double bmi, Goal goal);
}
