package com.wello.wellobackend.model;

import com.wello.wellobackend.enums.ActivityLevel;
import com.wello.wellobackend.enums.Goal;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class History implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @Column(name = "weight")
    private double weight;

    @Column(name = "height")
    private int height;

    @Column(name = "goal")
    @Enumerated(EnumType.STRING)
    private Goal goal;

    @Column(name = "activity_level")
    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;
}
