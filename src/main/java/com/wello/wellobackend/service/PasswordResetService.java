package com.wello.wellobackend.service;

public interface PasswordResetService {
    /**
     * Initiate password reset process by sending OTP to user's email
     * 
     * @param email User's email address
     * @return Verification token to be used in OTP verification
     */
    String initiatePasswordReset(String email);

    /**
     * Verify the OTP and generate reset token
     * 
     * @param email             User's email address
     * @param otp               OTP code from email
     * @param verificationToken Token from initiatePasswordReset
     * @return Reset token to be used in password reset
     */
    String verifyResetOtp(String email, String otp, String verificationToken);

    /**
     * Reset user's password using reset token
     * 
     * @param resetToken  Token from verifyResetOtp
     * @param newPassword New password to set
     */
    void resetPassword(String resetToken, String newPassword);
}
