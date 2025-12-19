package com.wello.wellobackend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "workout_tracker")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutTracker extends HealthTracker {
    @Column(name = "total_calories_burned")
    private int totalCaloriesBurned;

    @OneToMany(mappedBy = "workoutTracker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutDetail> details;
}
