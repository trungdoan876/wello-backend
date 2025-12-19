package com.wello.wellobackend.controller;

import com.wello.wellobackend.dto.requests.WorkoutLogRequest;
import com.wello.wellobackend.dto.responses.WorkoutLogResponse;
import com.wello.wellobackend.service.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workout")
public class WorkoutController {

    @Autowired
    private WorkoutService workoutService;

    @PostMapping("/log")
    public ResponseEntity<WorkoutLogResponse> logWorkout(@RequestBody WorkoutLogRequest request) {
        return ResponseEntity.ok(workoutService.logWorkout(request));
    }

    @org.springframework.web.bind.annotation.GetMapping("/exercises")
    public ResponseEntity<java.util.List<com.wello.wellobackend.dto.responses.ExerciseResponse>> getAllExercises() {
        return ResponseEntity.ok(workoutService.getAllExercises());
    }

    @org.springframework.web.bind.annotation.GetMapping("/calculate")
    public ResponseEntity<com.wello.wellobackend.dto.responses.WorkoutCalculationResponse> calculateCalories(
            @org.springframework.web.bind.annotation.RequestParam int userId,
            @org.springframework.web.bind.annotation.RequestParam int exerciseId,
            @org.springframework.web.bind.annotation.RequestParam int durationMinutes) {
        return ResponseEntity.ok(workoutService.calculateCalories(userId, exerciseId, durationMinutes));
    }
}
