package com.wello.wellobackend.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.wello.wellobackend.enums.SleepStatus;
import java.time.LocalDateTime;

/**
 * Response DTO cho sleep tracker
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SleepTrackerResponse {
    private Integer id;
    private LocalDateTime sleepTime;
    private LocalDateTime wakeTime;
    private Integer duration; // phút
    private Double durationHours; // giờ
    private Integer quality; // 1-5 sao
    private Double sleepEfficiency; // %
    private String sleepEfficiencyRating; // "Excellent", "Good", "Poor"
    private Double complianceRate; // %
    private String complianceRating; // "Optimal", "Acceptable", "Poor"
    private String notes;
    private LocalDateTime date; // Từ HealthTracker (theo ngày đi ngủ)
    private SleepStatus status; // PENDING | COMPLETED
}
