package com.wello.wellobackend.repository;

import com.wello.wellobackend.model.NotificationSettings;
import com.wello.wellobackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationSettingsRepository extends JpaRepository<NotificationSettings, Integer> {
    Optional<NotificationSettings> findByUser(User user);

    List<NotificationSettings> findByWaterReminderEnabledTrue();
}
