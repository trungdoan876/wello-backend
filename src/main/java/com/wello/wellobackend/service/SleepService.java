package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.requests.UpdateSleepTrackerRequest;
import com.wello.wellobackend.dto.responses.SleepTrackerResponse;
import com.wello.wellobackend.dto.responses.TodaySleepResponse;

import java.time.LocalDate;

/**
 * Interface for Sleep Tracking Service - Quick Log + Basic CRUD
 */
public interface SleepService {

    /**
     * Cập nhật sleep tracker
     */
    SleepTrackerResponse updateSleepTracker(int userId, int trackerId, UpdateSleepTrackerRequest request);

    /**
     * Xóa sleep tracker
     */
    void deleteSleepTracker(int userId, int trackerId);

    // ========== Quick Log Methods ==========

    /**
     * Log bedtime (Quick Log Step 1) - Ghi giờ đi ngủ
     */
    SleepTrackerResponse logBedtime(int userId, java.time.LocalDateTime bedtime);

    /**
     * Complete sleep (Quick Log Step 2) - Ghi giờ thức dậy
     */
    SleepTrackerResponse completeSleep(int userId, java.time.LocalDateTime wakeTime, Integer quality);

    /**
     * Get today's sleep record - Lấy giấc ngủ hôm nay
     */
    TodaySleepResponse getTodaySleep(int userId, LocalDate date);
}
