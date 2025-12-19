package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.responses.UserProfileResponse;

public interface ProfileService {
    UserProfileResponse getUserProfile(int userId);

    java.util.List<com.wello.wellobackend.model.History> getProfileHistory(int userId);

    com.wello.wellobackend.dto.responses.UserVerificationResponse verifyUser(int userId);
}
