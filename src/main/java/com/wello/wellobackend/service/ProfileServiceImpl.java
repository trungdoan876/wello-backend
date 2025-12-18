package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.responses.ProfileResponse;
import com.wello.wellobackend.model.Profile;
import com.wello.wellobackend.model.User;
import com.wello.wellobackend.repository.AuthRepository;
import com.wello.wellobackend.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AuthRepository authRepository;

    @Override
    public ProfileResponse getProfileByUserId(int userId) {
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findByUser(user);
        if (profile == null) {
            return null;
        }

        return ProfileResponse.builder()
                .idProfile(profile.getIdProfile())
                .userId(user.getIdUser())
                .fullname(profile.getFullname())
                .gender(profile.getGender() != null ? profile.getGender().name() : null)
                .age(profile.getAge())
                .height(profile.getHeight())
                .weight(profile.getWeight())
                .goal(profile.getGoal() != null ? profile.getGoal().name() : null)
                .activityLevel(profile.getActivityLevel() != null ? profile.getActivityLevel().name() : null)
                .avatarUrl(profile.getAvatarUrl())
                .surveyDate(profile.getSurveyDate())
                .build();
    }
}
