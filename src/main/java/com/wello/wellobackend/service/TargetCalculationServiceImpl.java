package com.wello.wellobackend.service;

import org.springframework.stereotype.Service;

/**
 * Implementation of TargetCalculationService
 * Uses Mifflin-St Jeor Equation for BMR calculation
 */
@Service
public class TargetCalculationServiceImpl implements TargetCalculationService {

    /**
     * Calculate BMR using Mifflin-St Jeor Equation
     * Men: BMR = (10 × weight) + (6.25 × height) − (5 × age) + 5
     * Women: BMR = (10 × weight) + (6.25 × height) − (5 × age) − 161
     */
    @Override
    public double calculateBMR(double weight, double height, int age, String gender) {
        if ("MALE".equalsIgnoreCase(gender)) {
            return (10 * weight) + (6.25 * height) - (5 * age) + 5;
        } else {
            return (10 * weight) + (6.25 * height) - (5 * age) - 161;
        }
    }

    /**
     * Calculate TDEE by multiplying BMR with activity level
     * Activity levels typically range from:
     * - 1.2 (sedentary)
     * - 1.375 (lightly active)
     * - 1.55 (moderately active)
     * - 1.725 (very active)
     * - 1.9 (extremely active)
     */
    @Override
    public double calculateTDEE(double bmr, double activityLevel) {
        return bmr * activityLevel;
    }

    /**
     * Calculate daily water intake based on weight and activity level
     * Formula: weight (kg) × activity coefficient (ml/kg)
     * 
     * Activity coefficients:
     * - Sedentary (1.2): 30 ml/kg
     * - Lightly active (1.375): 33 ml/kg
     * - Moderately active (1.55): 35 ml/kg
     * - Very active (1.725): 37 ml/kg
     * - Extremely active (1.9): 40 ml/kg
     */
    @Override
    public int calculateDailyWaterIntake(double weight, double activityLevel) {
        double coefficient;

        if (activityLevel <= 1.2) {
            coefficient = 30; // Sedentary
        } else if (activityLevel <= 1.375) {
            coefficient = 33; // Light active
        } else if (activityLevel <= 1.55) {
            coefficient = 35; // Moderate active
        } else if (activityLevel <= 1.725) {
            coefficient = 37; // Heavy active
        } else {
            coefficient = 40; // Very heavy active
        }

        return (int) (weight * coefficient);
    }

    @Override
    public int calculateExerciseCalories(double met, double weight, int durationMinutes) {
        return (int) (met * weight * (durationMinutes / 60.0));
    }
}
