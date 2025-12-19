package com.wello.wellobackend.controller;

import com.wello.wellobackend.dto.requests.WorkoutLogRequest;
import com.wello.wellobackend.dto.responses.DailyWorkoutSummaryResponse;
import com.wello.wellobackend.dto.responses.ExerciseResponse;
import com.wello.wellobackend.dto.responses.WorkoutCalculationResponse;
import com.wello.wellobackend.dto.responses.WorkoutLogResponse;
import com.wello.wellobackend.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/workout")
public class WorkoutController {

    @Autowired
    private WorkoutService workoutService;

    @PostMapping("/log")
    public ResponseEntity<WorkoutLogResponse> logWorkout(@RequestBody WorkoutLogRequest request) {
        return ResponseEntity.ok(workoutService.logWorkout(request));
    }

    @GetMapping("/exercises")
    public ResponseEntity<List<ExerciseResponse>> getAllExercises() {
        return ResponseEntity.ok(workoutService.getAllExercises());
    }

    @GetMapping("/calculate")
    public ResponseEntity<WorkoutCalculationResponse> calculateCalories(
            @RequestParam int userId,
            @RequestParam int exerciseId,
            @RequestParam int durationMinutes) {
        return ResponseEntity.ok(workoutService.calculateCalories(userId, exerciseId, durationMinutes));
    }

    @GetMapping("/daily")
    public ResponseEntity<DailyWorkoutSummaryResponse> getDailyWorkouts(
            @RequestParam int userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(workoutService.getDailyWorkouts(userId, date));
    }
}
