package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.responses.UserProfileResponse;
import com.wello.wellobackend.dto.responses.UserInfoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileService {
    UserProfileResponse getUserProfile(int userId);

    java.util.List<com.wello.wellobackend.model.History> getProfileHistory(int userId);

    com.wello.wellobackend.dto.responses.UserVerificationResponse verifyUser(int userId);

    UserInfoResponse getUserInfo(int userId);

    void uploadAvatar(int userId, MultipartFile file) throws IOException;

    void uploadAvatarBase64(int userId, String base64Image);
}
