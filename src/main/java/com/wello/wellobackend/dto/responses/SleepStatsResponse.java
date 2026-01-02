package com.wello.wellobackend.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO cho sleep statistics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SleepStatsResponse {
    private String period; // "week" hoặc "month"
    private Integer totalNightsTracked; // Số đêm đã ghi nhận
    private Integer totalNightsPossible; // Tổng số đêm trong kỳ
    private Double trackingRate; // % hoàn thành

    private Integer currentAge;
    private String ageGroup;
    private SleepRecommendation recommendedRange;

    private Double userGoal; // Mục tiêu của user
    private Double actualAverage; // Trung bình thực tế (giờ)
    private Double averageQuality; // Trung bình chất lượng
    private Double averageSleepEfficiency; // %
    private Double averageComplianceRate; // %
    private Double sleepDebt; // Nợ giấc ngủ (giờ)
    private String trend; // "improving", "stable", "declining"

    private List<String> insights; // Insights thông minh
    private List<SleepTrackerResponse> dailyTrackers; // 7 ngày gần nhất
}
