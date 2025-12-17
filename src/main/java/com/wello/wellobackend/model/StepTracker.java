package com.wello.wellobackend.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "step_tracker")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StepTracker extends HealthTracker{
    @Column(name = "steps", nullable = false)
    private int steps;
    @Column(name = "distance", nullable = false)
    private double distance;
}
