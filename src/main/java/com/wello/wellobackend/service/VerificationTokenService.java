package com.wello.wellobackend.service;

import java.util.Map;

public interface VerificationTokenService {
    String createVerificationToken(String email, String password, String otp);

    Map<String, String> verifyToken(String token, String userOtp);
}
