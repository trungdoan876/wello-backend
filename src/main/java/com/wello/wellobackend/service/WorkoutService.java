package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.requests.WorkoutLogRequest;
import com.wello.wellobackend.dto.responses.DailyWorkoutSummaryResponse;
import com.wello.wellobackend.dto.responses.ExerciseResponse;
import com.wello.wellobackend.dto.responses.WorkoutCalculationResponse;
import com.wello.wellobackend.dto.responses.WorkoutLogResponse;

import java.time.LocalDate;
import java.util.List;

public interface WorkoutService {
    WorkoutLogResponse logWorkout(WorkoutLogRequest request);

    List<ExerciseResponse> getAllExercises();

    WorkoutCalculationResponse calculateCalories(int userId, int exerciseId, int durationMinutes);

    DailyWorkoutSummaryResponse getDailyWorkouts(int userId, LocalDate date);
}
