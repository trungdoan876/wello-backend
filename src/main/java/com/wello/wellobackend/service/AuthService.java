package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.requests.AuthRequest;
import com.wello.wellobackend.dto.responses.AuthResponse;
import com.wello.wellobackend.dto.responses.OtpVerificationResult;

public interface AuthService {
    AuthResponse login(AuthRequest request);

    AuthResponse loginWithGoogle(String idToken) throws Exception;

    String sendRegistrationOtp(String email, String password);

    OtpVerificationResult verifyOtp(String verificationToken, String otp);

    Long registerUser(String email, String hashedPassword);

    String initiatePasswordReset(String email);

    String generateResetToken(String email);

    void resetPassword(String resetToken, String newPassword);
}
