package com.wello.wellobackend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "target")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Target {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

}
