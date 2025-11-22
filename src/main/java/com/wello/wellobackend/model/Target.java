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

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "calories_target")
    private int caloriesTarget;

    @Column(name = "carb_target")
    private int carbTarget;

    @Column(name = "fat_target")
    private int fatTarget;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

}
