package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.requests.WorkoutLogRequest;
import com.wello.wellobackend.dto.responses.WorkoutLogResponse;
import com.wello.wellobackend.model.*;
import com.wello.wellobackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class WorkoutServiceImpl implements WorkoutService {

        @Autowired
        private AuthRepository authRepository;

        @Autowired
        private WorkoutExerciseRepository workoutExerciseRepository;

        @Autowired
        private WorkoutTrackerRepository workoutTrackerRepository;

        @Autowired
        private WorkoutDetailRepository workoutDetailRepository;

        @Autowired
        private ProfileRepository profileRepository;

        @Autowired
        private NutritionTrackerRepository nutritionTrackerRepository;

        @Autowired
        private TargetCalculationService targetCalculationService;

        @Override
        @Transactional
        public WorkoutLogResponse logWorkout(WorkoutLogRequest request) {
                User user = authRepository.findById(request.getUserId())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                WorkoutExercise exercise = workoutExerciseRepository.findById(request.getExerciseId())
                                .orElseThrow(() -> new RuntimeException("Exercise not found"));

                Profile profile = profileRepository.findByUser(user);
                double weight = profile != null ? profile.getWeight() : 60.0; // Default weight if profile not found

                // 1. Calculate calories
                int caloriesBurned = targetCalculationService.calculateExerciseCalories(
                                exercise.getMetValue(),
                                weight,
                                request.getDurationMinutes());

                // 2. Save WorkoutTracker (one per day)
                LocalDateTime startOfDay = LocalDateTime.of(request.getDate(), LocalTime.MIN);
                LocalDateTime endOfDay = LocalDateTime.of(request.getDate(), LocalTime.MAX);

                WorkoutTracker tracker = workoutTrackerRepository.findByUserAndDate(user, startOfDay, endOfDay)
                                .orElseGet(() -> {
                                        WorkoutTracker newTracker = new WorkoutTracker();
                                        newTracker.setUser(user);
                                        newTracker.setDate(LocalDateTime.of(request.getDate(), LocalTime.now()));
                                        return workoutTrackerRepository.save(newTracker);
                                });

                // 3. Save WorkoutDetail
                WorkoutDetail detail = new WorkoutDetail();
                detail.setWorkoutTracker(tracker);
                detail.setWorkoutExercise(exercise);
                detail.setDurationMinutes(request.getDurationMinutes());
                workoutDetailRepository.save(detail);

                // 4. Update NutritionTracker
                NutritionTracker nutrition = nutritionTrackerRepository.findByUserAndDate(user, startOfDay, endOfDay)
                                .orElseGet(() -> {
                                        NutritionTracker newNutrition = new NutritionTracker();
                                        newNutrition.setUser(user);
                                        newNutrition.setDate(LocalDateTime.of(request.getDate(), LocalTime.now()));
                                        return newNutrition;
                                });

                nutrition.setCaloriesBurned(nutrition.getCaloriesBurned() + caloriesBurned);
                nutritionTrackerRepository.save(nutrition);

                return WorkoutLogResponse.builder()
                                .workoutId(detail.getId())
                                .exerciseName(exercise.getExerciseName())
                                .durationMinutes(request.getDurationMinutes())
                                .caloriesBurned(caloriesBurned)
                                .message("Workout logged successfully")
                                .build();
        }

        @Override
        public java.util.List<com.wello.wellobackend.dto.responses.ExerciseResponse> getAllExercises() {
                return workoutExerciseRepository.findAll().stream()
                                .map(e -> com.wello.wellobackend.dto.responses.ExerciseResponse.builder()
                                                .id(e.getIdExercise())
                                                .name(e.getExerciseName())
                                                .metValue(e.getMetValue())
                                                .build())
                                .collect(java.util.stream.Collectors.toList());
        }

        @Override
        public com.wello.wellobackend.dto.responses.WorkoutCalculationResponse calculateCalories(int userId,
                        int exerciseId, int durationMinutes) {
                User user = authRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                WorkoutExercise exercise = workoutExerciseRepository.findById(exerciseId)
                                .orElseThrow(() -> new RuntimeException("Exercise not found"));

                Profile profile = profileRepository.findByUser(user);
                double weight = profile != null ? profile.getWeight() : 60.0;

                int caloriesBurned = targetCalculationService.calculateExerciseCalories(
                                exercise.getMetValue(),
                                weight,
                                durationMinutes);

                return com.wello.wellobackend.dto.responses.WorkoutCalculationResponse.builder()
                                .caloriesBurned(caloriesBurned)
                                .metValue(exercise.getMetValue())
                                .userWeight(weight)
                                .durationMinutes(durationMinutes)
                                .build();
        }
}
