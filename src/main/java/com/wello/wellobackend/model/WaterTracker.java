package com.wello.wellobackend.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "water_tracker")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaterTracker extends HealthTracker {
    @Column(name = "amount_ml")
    private int amountMl;
}
