package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.responses.ProfileResponse;

public interface ProfileService {
    ProfileResponse getProfileByUserId(int userId);
}
