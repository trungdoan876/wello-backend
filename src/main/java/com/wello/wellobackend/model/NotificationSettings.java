package com.wello.wellobackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "notification_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSettings implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_settings")
    private int idSettings;

    @OneToOne
    @JoinColumn(name = "id_user", nullable = false, unique = true)
    private User user;

    @Column(name = "water_reminder_enabled", nullable = false)
    private boolean waterReminderEnabled = false;

    @Column(name = "reminder_start_hour", nullable = false)
    private int reminderStartHour = 8;

    @Column(name = "reminder_end_hour", nullable = false)
    private int reminderEndHour = 22;

    @Column(name = "reminder_interval_hours", nullable = false)
    private int reminderIntervalHours = 1;

    @Column(name = "reminder_interval_minutes", nullable = false)
    private int reminderIntervalMinutes = 0;
}
