package com.wello.wellobackend.model;

import com.wello.wellobackend.enums.MealType;
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

    @Column(name = "calories_burned")
    private int caloriesBurned; // Will be 0 for food logs

    @Column(name = "carbs", nullable = false)
    private int carbs;

    @Column(name = "protein", nullable = false)
    private int protein;

    @Column(name = "fat", nullable = false)
    private int fat;

    @Column(name = "amount_grams")
    private int amountGrams;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type")
    private MealType mealType;

    // Optional overrides when logging from favorites or manual edits
    @Column(name = "food_name_override")
    private String foodNameOverride;

    @Column(name = "calories_override")
    private Integer caloriesOverride;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_food")
    private Food food;
}
