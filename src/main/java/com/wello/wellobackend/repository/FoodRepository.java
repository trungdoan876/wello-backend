package com.wello.wellobackend.repository;

import com.wello.wellobackend.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {
    List<Food> findByFoodNameContainingIgnoreCase(String name);
}
