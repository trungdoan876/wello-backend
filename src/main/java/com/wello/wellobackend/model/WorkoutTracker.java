package com.wello.wellobackend.model;

import jakarta.persistence.CascadeType;
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
public class WorkoutTracker extends HealthTracker{
    @OneToMany(mappedBy = "workoutTracker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutDetail> details;
}
