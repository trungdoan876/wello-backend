package com.wello.wellobackend.service;

import com.wello.wellobackend.model.NotificationSettings;
import com.wello.wellobackend.model.User;
import com.wello.wellobackend.repository.AuthRepository;
import com.wello.wellobackend.repository.NotificationSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationSettingsServiceImpl implements NotificationSettingsService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private NotificationSettingsRepository notificationSettingsRepository;

    @Override
    public void updateWaterReminderSettings(int userId, boolean enabled, int startHour, int endHour, int intervalHours,
            int intervalMinutes) {
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        NotificationSettings settings = notificationSettingsRepository.findByUser(user)
                .orElseGet(() -> {
                    NotificationSettings newSettings = new NotificationSettings();
                    newSettings.setUser(user);
                    return newSettings;
                });

        settings.setWaterReminderEnabled(enabled);
        settings.setReminderStartHour(startHour);
        settings.setReminderEndHour(endHour);
        settings.setReminderIntervalHours(intervalHours);
        settings.setReminderIntervalMinutes(intervalMinutes);
        notificationSettingsRepository.save(settings);
    }
}
