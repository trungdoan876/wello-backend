package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.requests.RegisterRequest;
import com.wello.wellobackend.dto.responses.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

}
