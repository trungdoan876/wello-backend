package com.wello.wellobackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
    name = "daily_streak",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyStreak {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    // true nếu ngày đó có uống nước
    private boolean water;

    // true nếu ngày đó có ăn
    private boolean meal;
}
