package com.wello.wellobackend.controller;

import com.wello.wellobackend.dto.responses.DailyNutritionResponse;
import com.wello.wellobackend.service.WaterTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/water-intake")
public class WaterIntakeController {

    @Autowired
    private WaterTrackerService waterTrackerService;

    @GetMapping("/daily")
    public ResponseEntity<DailyNutritionResponse.WaterIntake> getDailyWaterIntake(
            @RequestParam int userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(waterTrackerService.getDailyWaterIntake(userId, date));
    }

    @PostMapping("/add")
    public ResponseEntity<String> addWaterIntake(
            @RequestBody com.wello.wellobackend.dto.requests.AddWaterIntakeRequest request) {
        waterTrackerService.addWaterIntake(request);
        return ResponseEntity.ok("Water intake added successfully");
    }
}
