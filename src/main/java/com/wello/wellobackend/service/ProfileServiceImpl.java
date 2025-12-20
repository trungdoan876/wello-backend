package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.responses.UserProfileResponse;
import com.wello.wellobackend.dto.responses.UserInfoResponse;
import com.wello.wellobackend.dto.responses.UserVerificationResponse;
import com.wello.wellobackend.enums.Goal;
import com.wello.wellobackend.model.*;
import com.wello.wellobackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Base64;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private FcmService fcmService;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private TargetRepository targetRepository;

    @Autowired
    private NutritionTrackerRepository nutritionTrackerRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Override
    public UserProfileResponse getUserProfile(int userId) {
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findByUser(user);
        Target target = targetRepository.findByUser(user);

        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        List<NutritionTracker> nutritionLogs = nutritionTrackerRepository.findByUserAndDateRange(user, todayStart,
                todayEnd);
        int totalCaloriesBurned = nutritionLogs.stream().mapToInt(NutritionTracker::getCaloriesBurned).sum();

        double currentWeight = profile != null ? profile.getWeight() : 0;
        double targetWeight = calculateTargetWeight(currentWeight, profile != null ? profile.getGoal() : null);

        return UserProfileResponse.builder()
                .userId(userId)
                .currentWeight(currentWeight)
                .targetWeight(targetWeight)
                .startDate(target != null && target.getStartDate() != null ? target.getStartDate().toLocalDate() : null)
                .goalType(profile != null ? profile.getGoal() : null)
                .dailyCalorieTarget(target != null ? target.getCaloriesTarget() : 0)
                .dailyCalorieBurned(totalCaloriesBurned)
                .dailyWaterTarget(target != null ? target.getWaterIntakeMl() : 0)
                .macroTargets(UserProfileResponse.MacroTargets.builder()
                        .carb(target != null ? target.getCarbTarget() : 0)
                        .protein(target != null ? target.getProteinTarget() : 0)
                        .fat(target != null ? target.getFatTarget() : 0)
                        .build())
                .build();
    }

    @Override
    public UserInfoResponse getUserInfo(int userId) {
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findByUser(user);

        UserInfoResponse.UserInfoResponseBuilder builder = UserInfoResponse.builder()
                .userId(user.getIdUser())
                .email(user.getEmail())
                .authProvider(user.getAuthProvider())
                .googleId(user.getGoogleId())
                .fcmToken(user.getFcmToken());

        if (profile != null) {
            builder
                    .fullname(profile.getFullname())
                    .gender(profile.getGender())
                    .age(profile.getAge())
                    .height(profile.getHeight())
                    .weight(profile.getWeight())
                    .goal(profile.getGoal())
                    .activityLevel(profile.getActivityLevel())
                    .avatarUrl(profile.getAvatarUrl())
                    .surveyDate(profile.getSurveyDate());
        }

        return builder.build();
    }

    private double calculateTargetWeight(double currentWeight, Goal goal) {
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
    public List<History> getProfileHistory(int userId) {
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return historyRepository.findByUserOrderByRecordedAtDesc(user);
    }

    @Override
    public UserVerificationResponse verifyUser(int userId) {
        boolean exists = authRepository.existsById(userId);
        if (exists) {
            return UserVerificationResponse.builder()
                    .exists(true)
                    .userId(userId)
                    .build();
        } else {
            return UserVerificationResponse.builder()
                    .exists(false)
                    .message("User not found")
                    .build();
        }
    }

    @Override
    public void uploadAvatar(int userId, MultipartFile file) throws IOException {
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findByUser(user);
        if (profile == null) {
            throw new RuntimeException("Profile not found for user");
        }

        // Convert file to Base64
        byte[] fileContent = file.getBytes();
        String base64Avatar = "data:" + file.getContentType() + ";base64,"
                + Base64.getEncoder().encodeToString(fileContent);

        // Save avatar to profile
        profile.setAvatarUrl(base64Avatar);
        profileRepository.save(profile);

        System.out.println("Avatar uploaded successfully for user: " + userId);
    }

    @Override
    public void uploadAvatarBase64(int userId, String base64Image) {
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findByUser(user);
        if (profile == null) {
            throw new RuntimeException("Profile not found for user");
        }

        // Validate base64 string
        if (base64Image == null || base64Image.trim().isEmpty()) {
            throw new RuntimeException("Base64 image cannot be empty");
        }

        // Save avatar to profile
        profile.setAvatarUrl(base64Image);
        profileRepository.save(profile);

        System.out.println("Avatar uploaded successfully for user: " + userId);
    }

    @Override
    public void updateFcmToken(int userId, String fcmToken) {
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFcmToken(fcmToken);
        authRepository.save(user);
    }

    @Override
    public void testPushNotification(int userId) {
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = user.getFcmToken();
        if (token != null && !token.isEmpty()) {
            fcmService.sendPushNotification(
                    token,
                    "Thông báo thử nghiệm",
                    "Nếu bạn thấy tin nhắn này, Firebase đã hoạt động tốt!");
        } else {
            throw new RuntimeException("FCM Token not found for this user");
        }
    }
}
