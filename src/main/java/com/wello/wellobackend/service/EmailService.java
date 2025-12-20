package com.wello.wellobackend.service;

public interface EmailService {
    void sendOtpEmail(String toEmail, String otp);
}
