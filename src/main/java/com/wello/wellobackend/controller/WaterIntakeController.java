package com.wello.wellobackend.controller;

import com.wello.wellobackend.dto.requests.AddWaterIntakeRequest;
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
            @RequestBody AddWaterIntakeRequest request) {
        waterTrackerService.addWaterIntake(request);
        return ResponseEntity.ok("Water intake added successfully");
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteWaterIntake(
            @RequestBody AddWaterIntakeRequest request) {
        try {
            waterTrackerService.deleteWaterIntake(request);
            return ResponseEntity.ok("250ml water intake removed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{waterTrackerId}")
    public ResponseEntity<String> deleteWaterIntakeById(@PathVariable int waterTrackerId) {
        try {
            waterTrackerService.deleteWaterIntakeById(waterTrackerId);
            return ResponseEntity.ok("Water intake deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }
}
