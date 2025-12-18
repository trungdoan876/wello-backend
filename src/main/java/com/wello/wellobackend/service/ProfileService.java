package com.wello.wellobackend.service;

<<<<<<< HEAD
import com.wello.wellobackend.dto.responses.ProfileResponse;

public interface ProfileService {
    ProfileResponse getProfileByUserId(int userId);
=======
import com.wello.wellobackend.dto.responses.UserProfileResponse;

public interface ProfileService {
    UserProfileResponse getUserProfile(int userId);

    com.wello.wellobackend.dto.responses.UserVerificationResponse verifyUser(int userId);
>>>>>>> 93df018a042264074b6de4041cb76a3dbf55cb85
}
