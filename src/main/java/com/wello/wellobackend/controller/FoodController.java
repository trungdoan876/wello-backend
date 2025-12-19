package com.wello.wellobackend.controller;

import com.wello.wellobackend.dto.responses.FoodResponse;
import com.wello.wellobackend.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @GetMapping("/all")
    public ResponseEntity<List<FoodResponse>> getAllFoods() {
        return ResponseEntity.ok(foodService.getAllFoods());
    }

    @GetMapping("/search")
    public ResponseEntity<List<FoodResponse>> searchFoods(@RequestParam String query) {
        return ResponseEntity.ok(foodService.searchFoods(query));
    }
}
