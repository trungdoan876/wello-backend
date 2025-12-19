package com.wello.wellobackend.service;

import com.wello.wellobackend.model.Profile;
import com.wello.wellobackend.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class ReminderScheduler {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private FcmService fcmService;

    // Run at the beginning of every hour
    @Scheduled(cron = "0 0 * * * *")
    public void sendWaterReminders() {
        System.out.println("Running water reminder scheduler...");
        List<Profile> activeProfiles = profileRepository.findByWaterReminderEnabledTrue();
        int currentHour = LocalTime.now().getHour();

        for (Profile profile : activeProfiles) {
            if (shouldSendReminder(profile, currentHour)) {
                String token = profile.getUser().getFcmToken();
                if (token != null && !token.isEmpty()) {
                    fcmService.sendPushNotification(
                            token,
                            "Uống nước thôi!",
                            "Đã đến lúc bổ sung nước để cơ thể luôn khỏe mạnh rồi bạn ơi!");
                }
            }
        }
    }

    private boolean shouldSendReminder(Profile profile, int currentHour) {
        // 1. Check if within time range
        if (currentHour < profile.getReminderStartHour() || currentHour > profile.getReminderEndHour()) {
            return false;
        }

        // 2. Check interval
        // Simple logic: (currentHour - startHour) % interval == 0
        int start = profile.getReminderStartHour();
        int interval = profile.getReminderIntervalHours();

        if (interval <= 0)
            interval = 1; // Safeguard

        return (currentHour - start) % interval == 0;
    }
}
