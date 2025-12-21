package com.wello.wellobackend.service;

import com.wello.wellobackend.enums.AuthProvider;
import com.wello.wellobackend.model.User;
import com.wello.wellobackend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Random;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String initiatePasswordReset(String email) {
        // Find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại trong hệ thống"));

        // Check if user is Google OAuth user (they don't have password)
        if (user.getAuthProvider() == AuthProvider.GOOGLE && user.getPassword() == null) {
            throw new RuntimeException("Tài khoản Google không thể đặt lại mật khẩu. Vui lòng đăng nhập bằng Google.");
        }

        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Create verification token (reuse existing service)
        // We pass empty password since we're not creating account, just verifying email
        String verificationToken = verificationTokenService.createVerificationToken(email, "", otp);

        // Send password reset email
        emailService.sendPasswordResetEmail(email, otp);

        return verificationToken;
    }

    @Override
    public String verifyResetOtp(String email, String otp, String verificationToken) {
        // Verify OTP using existing verification service
        Map<String, String> tokenData = verificationTokenService.verifyToken(verificationToken, otp);

        String tokenEmail = tokenData.get("email");

        // Verify email matches
        if (!email.equals(tokenEmail)) {
            throw new RuntimeException("Email không khớp với token");
        }

        // Verify user exists
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại trong hệ thống"));

        // Generate reset token (JWT) valid for 15 minutes
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        String resetToken = Jwts.builder()
                .setSubject(email)
                .claim("userId", user.getIdUser())
                .claim("purpose", "password_reset")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000)) // 15 minutes
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return resetToken;
    }

    @Override
    public void resetPassword(String resetToken, String newPassword) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

            // Parse and validate reset token
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(resetToken)
                    .getBody();

            // Verify token purpose
            String purpose = claims.get("purpose", String.class);
            if (!"password_reset".equals(purpose)) {
                throw new RuntimeException("Token không hợp lệ");
            }

            // Get email from token
            String email = claims.getSubject();

            // Find user
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

            // Hash new password
            String hashedPassword = passwordEncoder.encode(newPassword);

            // Update password
            user.setPassword(hashedPassword);
            userRepository.save(user);

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new RuntimeException("Token đã hết hạn. Vui lòng yêu cầu đặt lại mật khẩu mới.");
        } catch (Exception e) {
            throw new RuntimeException("Token không hợp lệ: " + e.getMessage());
        }
    }
}
