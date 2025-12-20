package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.responses.UserProfileResponse;
import com.wello.wellobackend.dto.responses.UserInfoResponse;
import com.wello.wellobackend.dto.responses.UserVerificationResponse;
import com.wello.wellobackend.model.History;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProfileService {
    UserProfileResponse getUserProfile(int userId);

    List<History> getProfileHistory(int userId);

    UserVerificationResponse verifyUser(int userId);

    UserInfoResponse getUserInfo(int userId);

    void uploadAvatar(int userId, MultipartFile file) throws IOException;

    void uploadAvatarBase64(int userId, String base64Image);

    void updateFcmToken(int userId, String fcmToken);

    void testPushNotification(int userId);
}
