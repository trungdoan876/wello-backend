package com.wello.wellobackend.controller;

import com.wello.wellobackend.dto.responses.DailyNutritionResponse;
import com.wello.wellobackend.dto.responses.WeeklyOverviewResponse;
import com.wello.wellobackend.service.NutritionTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/nutrition")
public class NutritionController {

    @Autowired
    private NutritionTrackerService nutritionTrackerService;

    @GetMapping("/daily-summary")
    public ResponseEntity<DailyNutritionResponse> getDailySummary(
            @RequestParam int userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(nutritionTrackerService.getDailySummary(userId, date));
    }

    @GetMapping("/week-overview")
    public ResponseEntity<WeeklyOverviewResponse> getWeeklyOverview(
            @RequestParam int userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        return ResponseEntity.ok(nutritionTrackerService.getWeeklyOverview(userId, startDate));
    }

    @PostMapping("/log-food")
    public ResponseEntity<com.wello.wellobackend.dto.responses.LogFoodResponse> logFood(
            @RequestBody com.wello.wellobackend.dto.requests.LogFoodRequest request) {
        return ResponseEntity.ok(nutritionTrackerService.logFood(request));
    }
}
