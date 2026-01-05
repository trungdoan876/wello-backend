package com.wello.wellobackend.model;

import com.wello.wellobackend.enums.SleepStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sleep_tracker")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SleepTracker extends HealthTracker {

    @Column(name = "sleep_time", nullable = false)
    private java.time.LocalDateTime sleepTime;

    @Column(name = "wake_time") // Nullable for PENDING status
    private java.time.LocalDateTime wakeTime;

    @Column(name = "duration") // Nullable for PENDING status
    private Integer duration; // Tổng phút ngủ

    @Column(name = "quality") // Nullable for PENDING status
    private Integer quality; // 1-5 sao

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "sleep_efficiency")
    private Double sleepEfficiency; // %

    @Column(name = "compliance_rate")
    private Double complianceRate; // %

    // Quick Log status
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SleepStatus status; // PENDING | COMPLETED
}
