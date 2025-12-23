package com.wello.wellobackend.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetResponse {
    private boolean success;
    private String message;
    private String resetToken; // Optional, only for verify-reset-otp
    private String verificationToken; // Optional, only for forgot-password

    // Constructor for simple success/message responses
    public PasswordResetResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
