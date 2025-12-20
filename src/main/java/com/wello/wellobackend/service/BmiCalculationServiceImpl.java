package com.wello.wellobackend.service;

import com.wello.wellobackend.enums.Goal;
import org.springframework.stereotype.Service;

@Service
public class BmiCalculationServiceImpl implements BmiCalculationService {

    @Override
    public double calculateBMI(int weight, int height) {
        // BMI = weight(kg) / (height(m))^2
        double heightInMeters = height / 100.0;
        double bmi = weight / (heightInMeters * heightInMeters);
        return Math.round(bmi * 100.0) / 100.0; // Round to 2 decimal places
    }

    @Override
    public String getBMIStatus(double bmi) {
        if (bmi < 18.5) {
            return "UNDERWEIGHT";
        } else if (bmi < 25) {
            return "NORMAL";
        } else if (bmi < 30) {
            return "OVERWEIGHT";
        } else {
            return "OBESE";
        }
    }

    @Override
    public String getBMIStatusText(String status) {
        switch (status) {
            case "UNDERWEIGHT":
                return "Thiếu cân";
            case "NORMAL":
                return "Bình thường";
            case "OVERWEIGHT":
                return "Thừa cân";
            case "OBESE":
                return "Béo phì";
            default:
                return "Không xác định";
        }
    }

    @Override
    public String getGoalWarning(double bmi, Goal goal) {
        if (bmi < 18.5 && goal == Goal.LOSE_WEIGHT) {
            return "⚠️ Bạn đã thiếu cân (BMI < 18.5), không nên giảm cân thêm. Hãy cân nhắc lại mục tiêu!";
        } else if (bmi >= 25 && goal == Goal.GAIN_WEIGHT) {
            return "⚠️ Bạn đã thừa cân (BMI ≥ 25), nên cân nhắc lại mục tiêu tăng cân!";
        }
        return null; // No warning
    }
}
