package com.wello.wellobackend.dto.responses;

public class OtpVerificationResult {
    private String email;
    private String hashedPassword;
    private String resetToken;
    private String type; // "registration" or "password_reset"

    public OtpVerificationResult(String email, String hashedPassword, String resetToken, String type) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.resetToken = resetToken;
        this.type = type;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
