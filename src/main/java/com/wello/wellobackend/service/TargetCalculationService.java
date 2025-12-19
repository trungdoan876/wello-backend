package com.wello.wellobackend.service;

/**
 * Service interface for calculating health and nutrition targets
 * Handles BMR, TDEE, and calorie goal calculations based on user profile data
 */
public interface TargetCalculationService {

    /**
     * Calculate Basal Metabolic Rate (BMR)
     * The minimum amount of calories the body needs to sustain life
     * 
     * @param weight in kilograms
     * @param height in centimeters
     * @param age    in years
     * @param gender "MALE" or "FEMALE"
     * @return BMR value in calories
     */
    double calculateBMR(double weight, double height, int age, String gender);

    /**
     * Calculate Total Daily Energy Expenditure (TDEE)
     * Total calories burned per day including activity level
     * 
     * @param bmr           Basal Metabolic Rate
     * @param activityLevel activity multiplier (e.g., 1.2 for sedentary, 1.9 for
     *                      very active)
     * @return TDEE value in calories
     */
    double calculateTDEE(double bmr, double activityLevel);

    /**
     * Calculate recommended daily water intake
     * Based on body weight and activity level
     * 
     * @param weight        in kilograms
     * @param activityLevel activity multiplier (e.g., 1.2 for sedentary, 1.9 for
     *                      very active)
     * @return recommended water intake in milliliters
     */
    int calculateDailyWaterIntake(double weight, double activityLevel);

    /**
     * Calculate calories burned from exercise
     * Formula: MET * weight (kg) * (duration (mins) / 60.0)
     * 
     * @param met             Metabolic Equivalent of Task
     * @param weight          in kilograms
     * @param durationMinutes duration of exercise in minutes
     * @return calories burned
     */
    int calculateExerciseCalories(double met, double weight, int durationMinutes);
}
