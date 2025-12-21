package com.wello.wellobackend.service;

import com.wello.wellobackend.model.User;
import com.wello.wellobackend.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthRepository authRepository;

    /**
     * Generate a 6-digit OTP
     */
    @Override
    public String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    /**
     * Send OTP via email and create verification token
     * Throws RuntimeException if email already exists
     */
    @Override
    public String sendOtpEmail(String email, String password) {
        // Check if email already exists
        User existingUser = authRepository.findByEmail(email);
        if (existingUser != null) {
            throw new RuntimeException("Email already registered");
        }

        // Generate OTP
        String otp = generateOtp();

        // Create verification token
        String verificationToken = verificationTokenService.createVerificationToken(email, password, otp);

        // Send OTP email
        emailService.sendOtpEmail(email, otp);

        return verificationToken;
    }
}
