package com.wello.wellobackend.service;

public interface BmiCalculationService {
    double calculateBMI(int weight, int height);

    String getBMIStatus(double bmi);

    String getBMIStatusText(String status);

    String getGoalWarning(double bmi, com.wello.wellobackend.enums.Goal goal);
}
