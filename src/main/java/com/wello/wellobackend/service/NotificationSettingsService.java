package com.wello.wellobackend.service;

public interface NotificationSettingsService {
    void updateWaterReminderSettings(int userId, boolean enabled, int startHour, int endHour, int intervalHours,
            int intervalMinutes);
}
