package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.requests.AuthRequest;
import com.wello.wellobackend.dto.responses.AuthResponse;

public interface AuthService {
    AuthResponse register(AuthRequest request);

    AuthResponse login(AuthRequest request);

    AuthResponse loginWithGoogle(String idToken) throws Exception;
}
