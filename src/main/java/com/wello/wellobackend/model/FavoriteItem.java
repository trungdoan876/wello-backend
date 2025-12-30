package com.wello.wellobackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "favorite_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_favorite", nullable = false)
    @JsonIgnore
    private Favorite favorite;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_food", nullable = false)
    private Food food;

    @Column(name = "amount_grams", nullable = false)
    private int amountGrams;
}
