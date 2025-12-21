package com.wello.wellobackend.service;

public interface OtpService {
    /**
     * Generate a 6-digit OTP
     * 
     * @return OTP string (e.g., "123456")
     */
    String generateOtp();

    /**
     * Send OTP via email and create verification token
     * 
     * @param email    User email
     * @param password User password (or empty string for password reset)
     * @return Verification token (JWT)
     */
    String sendOtpEmail(String email, String password);
}
