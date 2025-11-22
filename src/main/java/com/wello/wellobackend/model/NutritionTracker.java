package com.wello.wellobackend.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nutrition_tracker")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NutritionTracker extends HealthTracker{
    @Column(name = "calories_consumed")
    private int caloriesConsumed;

    @Column(name = "calories_burned")
    private int caloriesBurned;

    @Column(name = "carbs")
    private int carbs;
    @Column(name = "protein")
    private int protein;
    @Column(name = "carbs")
    private int fat;
}
