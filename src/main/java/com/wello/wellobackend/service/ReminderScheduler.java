package com.wello.wellobackend.service;

import com.wello.wellobackend.model.NotificationSettings;
import com.wello.wellobackend.repository.NotificationSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class ReminderScheduler {

    @Autowired
    private NotificationSettingsRepository notificationSettingsRepository;

    @Autowired
    private FcmService fcmService;

    // Run every minute for accurate minute-based intervals (change back to "0 0 * *
    // * *" for production hourly reminders)
    @Scheduled(cron = "0 * * * * *")
    public void sendWaterReminders() {
        System.out.println("Running water reminder scheduler...");
        List<NotificationSettings> activeSettings = notificationSettingsRepository.findByWaterReminderEnabledTrue();
        int currentHour = LocalTime.now().getHour();

        for (NotificationSettings settings : activeSettings) {
            if (shouldSendReminder(settings, currentHour)) {
                String token = settings.getUser().getFcmToken();
                if (token != null && !token.isEmpty()) {
                    fcmService.sendPushNotification(
                            token,
                            "Uống nước thôi!",
                            "Đã đến lúc bổ sung nước để cơ thể luôn khỏe mạnh rồi bạn ơi!");
                }
            }
        }
    }

    private boolean shouldSendReminder(NotificationSettings settings, int currentHour) {
        // 1. Check if within time range
        if (currentHour < settings.getReminderStartHour() || currentHour > settings.getReminderEndHour()) {
            return false;
        }

        // 2. Calculate total interval in minutes
        int intervalHours = settings.getReminderIntervalHours();
        int intervalMinutes = settings.getReminderIntervalMinutes();
        int totalIntervalMinutes = (intervalHours * 60) + intervalMinutes;

        if (totalIntervalMinutes <= 0) {
            totalIntervalMinutes = 60; // Default to 1 hour
        }

        // 3. Calculate minutes since start time
        int startHour = settings.getReminderStartHour();
        int currentMinute = java.time.LocalTime.now().getMinute();
        int minutesSinceStart = ((currentHour - startHour) * 60) + currentMinute;

        // 4. Check if current time matches the interval
        return minutesSinceStart >= 0 && minutesSinceStart % totalIntervalMinutes == 0;
    }
}
