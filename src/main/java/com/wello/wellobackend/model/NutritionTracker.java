package com.wello.wellobackend.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nutrition_tracker")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NutritionTracker extends HealthTracker {
    @Column(name = "calories_consumed", nullable = false)
    private int caloriesConsumed;

    @Column(name = "calories_burned", nullable = false)
    private int caloriesBurned;

    @Column(name = "carbs", nullable = false)
    private int carbs;

    @Column(name = "protein", nullable = false)
    private int protein;

    @Column(name = "fat", nullable = false)
    private int fat;
}
