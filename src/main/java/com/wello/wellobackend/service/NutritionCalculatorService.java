package com.wello.wellobackend.service;

public class NutritionCalculatorService {
    //bmr
    //the minimum amount of calories the body needs to sustain life
    public double calculateBMR(double weight, double height, int age, String gender){
        if("MALE".equalsIgnoreCase(gender)){
            return (10 * weight) + (6.25 * height) -(5 * age) + 5;
        }
        else {
            return (10 * weight) + (6.25 * height) -(5 * age) - 5;
        }
    }
    //tdee
    public double calculateTDEE(double bmr, double activityLevel){
        return bmr * activityLevel;
    }
    //calo goal
//    public double calculateGoalCalo(double tdee, String goal){
//
//    }
}
