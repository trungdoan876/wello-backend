package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.responses.SleepRecommendation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

/**
 * Service tính khuyến nghị giấc ngủ theo tuổi
 * Dựa trên National Sleep Foundation (NSF) 2015 recommendations
 */
@Service
public class SleepRecommendationService {

    /**
     * Lấy khuyến nghị giờ ngủ theo tuổi
     * 
     * @param age tuổi của user
     * @return SleepRecommendation với min/max/optimal hours
     */
    public SleepRecommendation getRecommendationByAge(int age) {
        if (age >= 3 && age <= 5) {
            return new SleepRecommendation(10, 13, 11.5, "Trẻ mẫu giáo (3-5 tuổi)");
        } else if (age >= 6 && age <= 13) {
            return new SleepRecommendation(9, 11, 10.0, "Trẻ em (6-13 tuổi)");
        } else if (age >= 14 && age <= 17) {
            return new SleepRecommendation(8, 10, 9.0, "Thanh thiếu niên (14-17 tuổi)");
        } else if (age >= 18 && age <= 25) {
            return new SleepRecommendation(7, 9, 8.0, "Người trẻ (18-25 tuổi)");
        } else if (age >= 26 && age <= 64) {
            return new SleepRecommendation(7, 9, 8.0, "Người trưởng thành (26-64 tuổi)");
        } else { // 65+
            return new SleepRecommendation(7, 8, 7.5, "Người cao tuổi (65+ tuổi)");
        }
    }

    /**
     * Lấy giờ đi ngủ và thức dậy lý tưởng theo tuổi
     * 
     * @param age tuổi của user
     * @return SleepTimeRecommendation với bedtime và wake time
     */
    public SleepTimeRecommendation getOptimalSleepTime(int age) {
        if (age >= 3 && age <= 5) {
            return new SleepTimeRecommendation(
                    LocalTime.of(20, 0), // 20:00
                    LocalTime.of(7, 0) // 07:00
            );
        } else if (age >= 6 && age <= 13) {
            return new SleepTimeRecommendation(
                    LocalTime.of(20, 30), // 20:30
                    LocalTime.of(6, 30) // 06:30
            );
        } else if (age >= 14 && age <= 17) {
            return new SleepTimeRecommendation(
                    LocalTime.of(21, 30), // 21:30
                    LocalTime.of(6, 30) // 06:30
            );
        } else if (age >= 18 && age <= 64) {
            return new SleepTimeRecommendation(
                    LocalTime.of(22, 30), // 22:30
                    LocalTime.of(6, 30) // 06:30
            );
        } else { // 65+
            return new SleepTimeRecommendation(
                    LocalTime.of(21, 30), // 21:30
                    LocalTime.of(5, 30) // 05:30
            );
        }
    }

    /**
     * DTO cho sleep time recommendation
     */
    @Data
    @AllArgsConstructor
    public static class SleepTimeRecommendation {
        private LocalTime bedtime;
        private LocalTime wakeTime;
    }
}
