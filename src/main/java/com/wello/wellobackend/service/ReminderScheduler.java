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

    // Run every 5 minutes for testing (change back to "0 0 * * * *" for production)
    @Scheduled(cron = "0 */5 * * * *")
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

        // 2. Check interval
        // Simple logic: (currentHour - startHour) % interval == 0
        int start = settings.getReminderStartHour();
        int interval = settings.getReminderIntervalHours();

        if (interval <= 0)
            interval = 1; // Safeguard

        return (currentHour - start) % interval == 0;
    }
}
