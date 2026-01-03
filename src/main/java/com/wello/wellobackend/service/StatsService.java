package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.responses.SevenDayStatsResponse;

public interface StatsService {
    /**
     * Lấy thống kê 7 ngày gần nhất
     * @param userId ID của người dùng
     * @return SevenDayStatsResponse với số liệu:
     *         - daysWithSufficientWater: Số ngày uống đủ nước
     *         - daysWithWorkout: Số ngày tập luyện
     *         - daysExceedingCalories: Số ngày vượt calo
     */
    SevenDayStatsResponse getSevenDayStats(int userId);
}
