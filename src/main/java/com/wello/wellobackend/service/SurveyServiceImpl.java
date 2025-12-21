package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.requests.SurveyRequest;
import com.wello.wellobackend.dto.responses.AnswerOptionResponse;
import com.wello.wellobackend.dto.responses.QuestionResponse;
import com.wello.wellobackend.dto.responses.TargetResponse;
import com.wello.wellobackend.enums.ActivityLevel;
import com.wello.wellobackend.model.History;
import com.wello.wellobackend.model.Profile;
import com.wello.wellobackend.model.Target;
import com.wello.wellobackend.model.User;
import com.wello.wellobackend.repository.AuthRepository;
import com.wello.wellobackend.repository.HistoryRepository;
import com.wello.wellobackend.repository.ProfileRepository;
import com.wello.wellobackend.repository.SurveyQuestionRepository;
import com.wello.wellobackend.repository.TargetRepository;
import com.wello.wellobackend.utils.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurveyServiceImpl implements SurveyService {
    @Autowired
    private SurveyQuestionRepository surveyQuestionRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private TargetCalculationService targetCalculationService;

    @Autowired
    private TargetRepository targetRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Override
    public List<QuestionResponse> getListSurveyQuestion() {
        return surveyQuestionRepository.findAll()
                .stream()
                .map(q -> {
                    // Lấy options an toàn, key có thể null
                    List<AnswerOptionResponse> options = EnumUtils.getOptionsForQuestion(q.getQuestionKey());
                    return new QuestionResponse(
                            q.getIdQuestion(),
                            q.getQuestionKey(),
                            q.getQuestionType(),
                            q.getQuestion(),
                            q.getUnitLabel(),
                            options);
                })
                .collect(Collectors.toList());
    }

    @Override
    public TargetResponse processSurvey(SurveyRequest request) {
        // 1. Lưu hoặc Cập nhật Profile
        Profile profile = saveProfileFromSurvey(request);

        // 2. Tính toán Target
        TargetResponse targetResponse = calculateTarget(profile);

        // 3. Lưu Target vào database
        saveTargetToDatabase(profile.getUser(), targetResponse);

        return targetResponse;
    }

    private Profile saveProfileFromSurvey(SurveyRequest request) {
        User user = authRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        // Kiểm tra xem User đã có Profile chưa, nếu có thì update, chưa thì tạo mới
        Profile profile = profileRepository.findByUser(user);

        if (profile == null) {
            profile = new Profile();
        }

        profile.setUser(user);
        profile.setFullname(request.getFullname());
        profile.setGender(request.getGender());
        profile.setAge(request.getAge());
        profile.setHeight(request.getHeight());
        profile.setWeight(request.getWeight());
        profile.setGoal(request.getGoal());
        profile.setActivityLevel(request.getActivityLevel());
        profile.setSurveyDate(LocalDateTime.now());

        // Lưu targetWeight và tính weightGoalKg
        if (request.getTargetWeight() != null) {
            profile.setTargetWeight(request.getTargetWeight());
            // weightGoalKg = currentWeight - targetWeight
            // Dương: cần giảm, Âm: cần tăng
            int weightGoalKg = request.getWeight() - request.getTargetWeight();
            profile.setWeightGoalKg(weightGoalKg);
        }

        Profile savedProfile = profileRepository.save(profile);

        // 2. Lưu vào Lịch sử (Snapshot)
        History history = History.builder()
                .user(user)
                .weight(savedProfile.getWeight())
                .height(savedProfile.getHeight())
                .goal(savedProfile.getGoal())
                .activityLevel(savedProfile.getActivityLevel())
                .recordedAt(LocalDateTime.now())
                .build();
        historyRepository.save(history);

        return savedProfile;
    }

    private TargetResponse calculateTarget(Profile profile) {
        // --- 1. Tính BMR sử dụng TargetCalculationService ---
        double bmr = targetCalculationService.calculateBMR(
                profile.getWeight(),
                profile.getHeight(),
                profile.getAge(),
                profile.getGender().name());

        // --- 2. Tính TDEE sử dụng TargetCalculationService ---
        double activityMultiplier = getActivityMultiplier(profile.getActivityLevel());
        double tdee = targetCalculationService.calculateTDEE(bmr, activityMultiplier);

        // --- 3. Tính Calories Mục tiêu ---
        int targetCalories = (int) tdee;
        switch (profile.getGoal()) {
            case LOSE_WEIGHT:
                targetCalories -= 500;
                break;
            case GAIN_WEIGHT:
                targetCalories += 500;
                break;
            default:
                break;
        }

        // --- 4. Tính Macros (30P - 35C - 35F) ---
        int protein = (int) ((targetCalories * 0.3) / 4);
        int carbs = (int) ((targetCalories * 0.35) / 4);
        int fat = (int) ((targetCalories * 0.35) / 9);

        // --- 5. Tính BMI ---
        double heightM = profile.getHeight() / 100.0;
        double bmi = profile.getWeight() / (heightM * heightM);
        String bmiStatus = getBmiStatus(bmi);

        // --- 6. Tính lượng nước sử dụng TargetCalculationService ---
        int waterIntake = targetCalculationService.calculateDailyWaterIntake(
                profile.getWeight(),
                activityMultiplier);

        return TargetResponse.builder()
                .bmi(Math.round(bmi * 10.0) / 10.0)
                .bmiStatus(bmiStatus)
                .bmr(Math.round(bmr))
                .tdee(Math.round(tdee))
                .dailyCalories(targetCalories)
                .proteinGram(protein)
                .carbsGram(carbs)
                .fatGram(fat)
                .waterIntakeMl(waterIntake)
                .build();
    }

    private double getActivityMultiplier(ActivityLevel level) {
        switch (level) {
            case SEDENTARY:
                return 1.2;
            case LIGHT_ACTIVE:
                return 1.375;
            case MODERATE_ACTIVE:
                return 1.55;
            case HEAVY_ACTIVE:
                return 1.725;
            case VERY_HEAVY_ACTIVE:
                return 1.9;
            default:
                return 1.2;
        }
    }

    private String getBmiStatus(double bmi) {
        if (bmi < 18.5)
            return "Underweight";
        if (bmi < 24.9)
            return "Normal";
        if (bmi < 29.9)
            return "Overweight";
        return "Obese";
    }

    private void saveTargetToDatabase(User user, TargetResponse targetResponse) {
        // Kiểm tra xem user đã có Target chưa
        Target target = targetRepository.findByUser(user);

        if (target == null) {
            target = new Target();
            target.setUser(user);
            target.setStartDate(LocalDateTime.now());
        }

        // Cập nhật end date (30 ngày từ bây giờ)
        target.setEndDate(LocalDateTime.now().plusDays(30));

        // Lưu các chỉ số đã tính toán
        target.setBmi(targetResponse.getBmi());
        target.setBmiStatus(targetResponse.getBmiStatus());
        target.setBmr(targetResponse.getBmr());
        target.setTdee(targetResponse.getTdee());
        target.setCaloriesTarget(targetResponse.getDailyCalories());
        target.setProteinTarget(targetResponse.getProteinGram());
        target.setCarbTarget(targetResponse.getCarbsGram());
        target.setFatTarget(targetResponse.getFatGram());
        target.setWaterIntakeMl(targetResponse.getWaterIntakeMl());

        targetRepository.save(target);
    }
}
