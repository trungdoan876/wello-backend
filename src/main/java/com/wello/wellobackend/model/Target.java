package com.wello.wellobackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "target")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Target implements Serializable {
    @Id
    @Column(name = "id_target")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTarget;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "calories_target", nullable = false)
    private int caloriesTarget;

    @Column(name = "carb_target", nullable = false)
    private int carbTarget;

    @Column(name = "fat_target", nullable = false)
    private int fatTarget;

    @Column(name = "protein_target", nullable = false)
    private int proteinTarget;

    // Health metrics
    @Column(name = "bmi")
    private double bmi;

    @Column(name = "bmi_status")
    private String bmiStatus;

    @Column(name = "bmr")
    private double bmr;

    @Column(name = "tdee")
    private double tdee;

    // Hydration
    @Column(name = "water_intake_ml")
    private int waterIntakeMl;

    // Sleep goals
    @Column(name = "sleep_target_hours")
    private Double sleepTargetHours;

    @Column(name = "sleep_bedtime_target")
    private LocalTime sleepBedtimeTarget;

    @Column(name = "sleep_wake_time_target")
    private LocalTime sleepWakeTimeTarget;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

}
