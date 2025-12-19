package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.responses.UserProfileResponse;
import com.wello.wellobackend.model.NutritionTracker;
import com.wello.wellobackend.model.Profile;
import com.wello.wellobackend.model.Target;
import com.wello.wellobackend.model.User;
import com.wello.wellobackend.repository.AuthRepository;
import com.wello.wellobackend.repository.NutritionTrackerRepository;
import com.wello.wellobackend.repository.ProfileRepository;
import com.wello.wellobackend.repository.TargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private TargetRepository targetRepository;

    @Autowired
    private NutritionTrackerRepository nutritionTrackerRepository;

    @Autowired
    private com.wello.wellobackend.repository.HistoryRepository historyRepository;

    @Override
    public UserProfileResponse getUserProfile(int userId) {
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findByUser(user);
        Target target = targetRepository.findByUser(user);

        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        NutritionTracker nutritionTracker = nutritionTrackerRepository.findByUserAndDate(user, todayStart, todayEnd)
                .orElse(new NutritionTracker());

        double currentWeight = profile != null ? profile.getWeight() : 0;
        double targetWeight = calculateTargetWeight(currentWeight, profile != null ? profile.getGoal() : null);

        return UserProfileResponse.builder()
                .userId(userId)
                .currentWeight(currentWeight)
                .targetWeight(targetWeight)
                .startDate(target != null && target.getStartDate() != null ? target.getStartDate().toLocalDate() : null)
                .goalType(profile != null ? profile.getGoal() : null)
                .dailyCalorieTarget(target != null ? target.getCaloriesTarget() : 0)
                .dailyCalorieBurned(nutritionTracker.getCaloriesBurned())
                .dailyWaterTarget(target != null ? target.getWaterIntakeMl() : 0)
                .macroTargets(UserProfileResponse.MacroTargets.builder()
                        .carb(target != null ? target.getCarbTarget() : 0)
                        .protein(target != null ? target.getProteinTarget() : 0)
                        .fat(target != null ? target.getFatTarget() : 0)
                        .build())
                .build();
    }

    private double calculateTargetWeight(double currentWeight, com.wello.wellobackend.enums.Goal goal) {
        if (goal == null)
            return currentWeight;
        switch (goal) {
            case LOSE_WEIGHT:
                return currentWeight - 5;
            case GAIN_WEIGHT:
                return currentWeight + 3;
            case KEEP_FIT:
            default:
                return currentWeight;
        }
    }

    @Override
    public java.util.List<com.wello.wellobackend.model.History> getProfileHistory(int userId) {
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return historyRepository.findByUserOrderByRecordedAtDesc(user);
    }

    @Override
    public com.wello.wellobackend.dto.responses.UserVerificationResponse verifyUser(int userId) {
        boolean exists = authRepository.existsById(userId);
        if (exists) {
            return com.wello.wellobackend.dto.responses.UserVerificationResponse.builder()
                    .exists(true)
                    .userId(userId)
                    .build();
        } else {
            return com.wello.wellobackend.dto.responses.UserVerificationResponse.builder()
                    .exists(false)
                    .message("User not found")
                    .build();
        }
    }
}
