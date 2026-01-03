package com.wello.wellobackend.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO cho khuyến nghị giấc ngủ theo tuổi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SleepRecommendation {
    private Integer minHours; // Số giờ tối thiểu
    private Integer maxHours; // Số giờ tối đa
    private Double optimalHours; // Số giờ tối ưu
    private String ageGroup; // Nhóm tuổi
}
