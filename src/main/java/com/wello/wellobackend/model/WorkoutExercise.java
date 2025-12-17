package com.wello.wellobackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "workout_exercise")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExercise implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idExercise;
    @Column(name = "name")
    private String exerciseName;
    @Column(name = "met_value")
    private int metValue; //Calo = met * cân nặng * giờ
}
