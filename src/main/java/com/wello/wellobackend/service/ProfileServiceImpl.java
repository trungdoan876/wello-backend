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

    @Autowired
    private TargetCalculationService targetCalculationService;

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
    public void updateFullname(int userId, String fullname) {
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findByUser(user);
        if (profile == null) {
            throw new RuntimeException("Profile not found for user");
        }

        if (fullname == null || fullname.trim().isEmpty()) {
            throw new RuntimeException("Fullname cannot be empty");
        }

        profile.setFullname(fullname);
        profileRepository.save(profile);

        System.out.println("Fullname updated successfully for user: " + userId);
    }

    @Override
    public void updateFcmToken(int userId, String fcmToken) {
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFcmToken(fcmToken);
        authRepository.save(user);
    }

    @Override
    public void updateGender(int userId, com.wello.wellobackend.enums.Gender gender) {
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findByUser(user);
        if (profile == null) {
            throw new RuntimeException("Profile not found for user");
        }

        if (gender == null) {
            throw new RuntimeException("Gender cannot be null");
        }

        profile.setGender(gender);
        profileRepository.save(profile);

        // Save to history
        saveToHistory(user, profile);

        // Recalculate target
        recalculateTarget(user, profile);

        System.out.println("Gender updated successfully for user: " + userId);
    }

    @Override
    public void updateAge(int userId, int age) {
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findByUser(user);
        if (profile == null) {
            throw new RuntimeException("Profile not found for user");
        }

        if (age <= 0 || age > 150) {
            throw new RuntimeException("Age must be between 1 and 150");
        }

        profile.setAge(age);
        profileRepository.save(profile);

        // Recalculate target (age affects BMR and TDEE)
        recalculateTarget(user, profile);

        System.out.println("Age updated successfully for user: " + userId);
    }

    @Override
    public void updateHeight(int userId, int height) {
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findByUser(user);
        if (profile == null) {
            throw new RuntimeException("Profile not found for user");
        }

        if (height <= 0 || height > 300) {
            throw new RuntimeException("Height must be between 1 and 300 cm");
        }

        profile.setHeight(height);
        profileRepository.save(profile);

        // Save to history
        saveToHistory(user, profile);

        // Recalculate target (height affects BMR, BMI, water intake)
        recalculateTarget(user, profile);

        System.out.println("Height updated successfully for user: " + userId);
    }

    @Override
    public void updateWeight(int userId, int weight) {
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findByUser(user);
        if (profile == null) {
            throw new RuntimeException("Profile not found for user");
        }

        if (weight <= 0 || weight > 500) {
            throw new RuntimeException("Weight must be between 1 and 500 kg");
        }

        profile.setWeight(weight);
        profileRepository.save(profile);

        // Save to history - weight tracking is important
        saveToHistory(user, profile);

        // Recalculate target (weight affects BMR, TDEE, BMI, water intake, calorie goals)
        recalculateTarget(user, profile);

        System.out.println("Weight updated successfully for user: " + userId);
    }

    @Override
    public void updateGoal(int userId, com.wello.wellobackend.enums.Goal goal) {
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findByUser(user);
        if (profile == null) {
            throw new RuntimeException("Profile not found for user");
        }

        if (goal == null) {
            throw new RuntimeException("Goal cannot be null");
        }

        profile.setGoal(goal);
        profileRepository.save(profile);

        // Save to history
        saveToHistory(user, profile);

        // Recalculate target (goal affects calorie target)
        recalculateTarget(user, profile);

        System.out.println("Goal updated successfully for user: " + userId);
    }

    @Override
    public void updateActivityLevel(int userId, com.wello.wellobackend.enums.ActivityLevel activityLevel) {
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findByUser(user);
        if (profile == null) {
            throw new RuntimeException("Profile not found for user");
        }

        if (activityLevel == null) {
            throw new RuntimeException("Activity level cannot be null");
        }

        profile.setActivityLevel(activityLevel);
        profileRepository.save(profile);

        // Save to history
        saveToHistory(user, profile);

        // Recalculate target (activity level affects TDEE and water intake)
        recalculateTarget(user, profile);

        System.out.println("Activity level updated successfully for user: " + userId);
    }

    /**
     * Save current profile changes to history record
     */
    private void saveToHistory(User user, Profile profile) {
        try {
            History history = History.builder()
                    .user(user)
                    .weight(profile.getWeight())
                    .height(profile.getHeight())
                    .goal(profile.getGoal())
                    .activityLevel(profile.getActivityLevel())
                    .recordedAt(LocalDateTime.now())
                    .build();

            historyRepository.save(history);
            System.out.println("History record saved for user: " + user.getIdUser());
        } catch (Exception e) {
            System.err.println("Error saving history: " + e.getMessage());
        }
    }

    /**
     * Recalculate target based on updated profile information
     */
    private void recalculateTarget(User user, Profile profile) {
        try {
            Target target = targetRepository.findByUser(user);
            if (target == null) {
                System.out.println("No target found for user, creating new one");
                target = new Target();
                target.setUser(user);
                target.setStartDate(LocalDateTime.now());
                target.setEndDate(LocalDateTime.now().plusMonths(1));
            }

            // Calculate BMI and BMR
            double bmi = calculateBMI(profile.getWeight(), profile.getHeight());
            String bmiStatus = getBMIStatus(bmi);
            double bmr = targetCalculationService.calculateBMR(
                    profile.getWeight(),
                    profile.getHeight(),
                    profile.getAge(),
                    profile.getGender().toString()
            );

            // Get activity multiplier
            double activityMultiplier = getActivityMultiplier(profile.getActivityLevel());

            // Calculate TDEE
            double tdee = targetCalculationService.calculateTDEE(bmr, activityMultiplier);

            // Calculate calorie goals based on objective
            int calorieTarget = calculateCalorieTarget(tdee, profile.getGoal());

            // Calculate macro targets (40% carbs, 30% protein, 30% fat)
            int carbTarget = (calorieTarget * 40) / 400; // 4 cal per gram carbs
            int proteinTarget = (calorieTarget * 30) / 400; // 4 cal per gram protein
            int fatTarget = (calorieTarget * 30) / 900; // 9 cal per gram fat

            // Calculate water intake
            int waterIntake = targetCalculationService.calculateDailyWaterIntake(
                    profile.getWeight(),
                    activityMultiplier
            );

            // Update target
            target.setBmi(bmi);
            target.setBmiStatus(bmiStatus);
            target.setBmr(bmr);
            target.setTdee(tdee);
            target.setCaloriesTarget(calorieTarget);
            target.setCarbTarget(carbTarget);
            target.setProteinTarget(proteinTarget);
            target.setFatTarget(fatTarget);
            target.setWaterIntakeMl(waterIntake);

            targetRepository.save(target);
            System.out.println("Target recalculated successfully for user: " + user.getIdUser());
        } catch (Exception e) {
            System.err.println("Error recalculating target: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Calculate BMI: weight(kg) / (height(m))^2
     */
    private double calculateBMI(double weight, double height) {
        double heightInMeters = height / 100.0;
        return weight / (heightInMeters * heightInMeters);
    }

    /**
     * Get BMI status based on BMI value
     */
    private String getBMIStatus(double bmi) {
        if (bmi < 18.5) return "UNDERWEIGHT";
        if (bmi < 25) return "NORMAL";
        if (bmi < 30) return "OVERWEIGHT";
        return "OBESE";
    }

    /**
     * Get activity multiplier from ActivityLevel enum
     */
    private double getActivityMultiplier(com.wello.wellobackend.enums.ActivityLevel level) {
        if (level == null) {
            return 1.5;
        }
        return level.getHeSoTDEE();
    }

    /**
     * Calculate calorie target based on goal
     */
    private int calculateCalorieTarget(double tdee, com.wello.wellobackend.enums.Goal goal) {
        switch (goal) {
            case LOSE_WEIGHT:
                return (int) (tdee * 0.85); // 15% deficit
            case GAIN_WEIGHT:
                return (int) (tdee * 1.15); // 15% surplus
            case KEEP_FIT:
            default:
                return (int) tdee;
        }
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
