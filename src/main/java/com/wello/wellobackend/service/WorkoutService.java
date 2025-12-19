package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.requests.WorkoutLogRequest;
import com.wello.wellobackend.dto.responses.WorkoutLogResponse;

public interface WorkoutService {
    WorkoutLogResponse logWorkout(WorkoutLogRequest request);

    java.util.List<com.wello.wellobackend.dto.responses.ExerciseResponse> getAllExercises();

    com.wello.wellobackend.dto.responses.WorkoutCalculationResponse calculateCalories(int userId, int exerciseId,
            int durationMinutes);
}
