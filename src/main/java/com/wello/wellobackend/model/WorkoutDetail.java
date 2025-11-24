package com.wello.wellobackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "workout_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "duration_minutes")
    private int durationMinutes;

    @ManyToOne
    @JoinColumn(name = "tracker_id")
    private WorkoutTracker workoutTracker;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private WorkoutExercise workoutExercise;
}
