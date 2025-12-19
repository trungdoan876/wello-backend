package com.wello.wellobackend.model;

import com.wello.wellobackend.enums.ActivityLevel;
import com.wello.wellobackend.enums.Gender;
import com.wello.wellobackend.enums.Goal;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile implements Serializable {
    @Id
    @Column(name = "id_profile")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProfile;

    @Column(name = "full_name", nullable = false)
    private String fullname;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "height", nullable = false)
    private int height;

    @Column(name = "weight", nullable = false)
    private int weight;

    @Column(name = "target_goal", nullable = false)
    @Enumerated(EnumType.STRING)
    private Goal goal;

    @Column(name = "activity_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;

    @Column(name = "avatar_url", columnDefinition = "LONGTEXT")
    private String avatarUrl; // Base64 encoded image

    @Column(name = "survey_date", nullable = false)
    private LocalDateTime surveyDate;

    @Column(name = "water_reminder_enabled", nullable = false)
    private boolean waterReminderEnabled = false;

    @Column(name = "reminder_start_hour")
    private int reminderStartHour = 8;

    @Column(name = "reminder_end_hour")
    private int reminderEndHour = 22;

    @Column(name = "reminder_interval_hours")
    private int reminderIntervalHours = 1;

    @OneToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;
}
