package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.requests.WorkoutLogRequest;
import com.wello.wellobackend.dto.responses.DailyWorkoutSummaryResponse;
import com.wello.wellobackend.dto.responses.ExerciseResponse;
import com.wello.wellobackend.dto.responses.WorkoutCalculationResponse;
import com.wello.wellobackend.dto.responses.WorkoutLogResponse;
import com.wello.wellobackend.model.*;
import com.wello.wellobackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                detail.setCaloriesBurned(caloriesBurned);
                workoutDetailRepository.save(detail);

                // Update total in Tracker
                tracker.setTotalCaloriesBurned(tracker.getTotalCaloriesBurned() + caloriesBurned);
                workoutTrackerRepository.save(tracker);

                // 4. Create new NutritionTracker entry for workout calories
                NutritionTracker nutrition = new NutritionTracker();
                nutrition.setUser(user);
                nutrition.setDate(LocalDateTime.of(request.getDate(), LocalTime.now()));
                nutrition.setCaloriesBurned(caloriesBurned);
                nutrition.setCaloriesConsumed(0);
                nutrition.setCarbs(0);
                nutrition.setProtein(0);
                nutrition.setFat(0);

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
        public List<ExerciseResponse> getAllExercises() {
                return workoutExerciseRepository.findAll().stream()
                                .map(e -> ExerciseResponse.builder()
                                                .id(e.getIdExercise())
                                                .name(e.getExerciseName())
                                                .metValue(e.getMetValue())
                                                .build())
                                .collect(Collectors.toList());
        }

        @Override
        public WorkoutCalculationResponse calculateCalories(int userId,
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

                return WorkoutCalculationResponse.builder()
                                .caloriesBurned(caloriesBurned)
                                .metValue(exercise.getMetValue())
                                .userWeight(weight)
                                .durationMinutes(durationMinutes)
                                .build();
        }

        @Override
        public DailyWorkoutSummaryResponse getDailyWorkouts(int userId,
                        LocalDate date) {
                User user = authRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                LocalDateTime startOfDay = LocalDateTime.of(date, LocalTime.MIN);
                LocalDateTime endOfDay = LocalDateTime.of(date, LocalTime.MAX);

                WorkoutTracker tracker = workoutTrackerRepository.findByUserAndDate(user, startOfDay, endOfDay)
                                .orElse(null);

                if (tracker == null) {
                        return DailyWorkoutSummaryResponse.builder()
                                        .totalCaloriesBurned(0)
                                        .workouts(new ArrayList<>())
                                        .build();
                }

                List<DailyWorkoutSummaryResponse.WorkoutDetailResponse> detailResponses = tracker
                                .getDetails().stream()
                                .map(d -> DailyWorkoutSummaryResponse.WorkoutDetailResponse
                                                .builder()
                                                .id(d.getId())
                                                .exerciseName(d.getWorkoutExercise().getExerciseName())
                                                .durationMinutes(d.getDurationMinutes())
                                                .caloriesBurned(d.getCaloriesBurned())
                                                .build())
                                .collect(Collectors.toList());

                return DailyWorkoutSummaryResponse.builder()
                                .totalCaloriesBurned(tracker.getTotalCaloriesBurned())
                                .workouts(detailResponses)
                                .build();
        }
}
