package com.wello.wellobackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "foods")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Food implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "food_name", nullable = false)
    private String foodName;

    @Column(name = "calories_per_100g")
    private int caloriesPer100g;

    @Column(name = "protein_per_100g")
    private double proteinPer100g;

    @Column(name = "carbs_per_100g")
    private double carbsPer100g;

    @Column(name = "fat_per_100g")
    private double fatPer100g;

    @Column(name = "image_url")
    private String imageUrl;
}
