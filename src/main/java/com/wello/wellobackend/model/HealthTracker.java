package com.wello.wellobackend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_tracker")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class HealthTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "date_tracker", nullable = false)
    private LocalDateTime date;
    @OneToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;
}
