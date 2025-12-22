package com.wello.wellobackend.controller;

import com.wello.wellobackend.dto.requests.AuthRequest;
import com.wello.wellobackend.dto.requests.ForgotPasswordRequest;
import com.wello.wellobackend.dto.requests.GoogleLoginRequest;
import com.wello.wellobackend.dto.requests.ResetPasswordRequest;
import com.wello.wellobackend.dto.responses.AuthResponse;
import com.wello.wellobackend.dto.responses.PasswordResetResponse;
import com.wello.wellobackend.dto.responses.OtpVerificationResult;
import com.wello.wellobackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private AuthService authService;

    // ============ LOGIN ============

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            AuthResponse response = authService.login(request);

            if (!response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse(false, "Đăng nhập thất bại!", -1, false));
        }
    }

    @PostMapping("/google-login")
    public ResponseEntity<AuthResponse> googleLogin(@RequestBody GoogleLoginRequest request) {
        try {
            AuthResponse response = authService.loginWithGoogle(request.getIdToken());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Google login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(false, "Xác thực Google thất bại!", -1, false));
        }
    }

    // ============ REGISTRATION WITH OTP ============

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");

            // Send OTP via AuthService
            String verificationToken = authService.sendRegistrationOtp(email, password);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "verificationToken", verificationToken,
                    "message", "Mã OTP đã được gửi đến email"));

        } catch (RuntimeException e) {
            System.err.println("Send OTP error: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()));
        } catch (Exception e) {
            System.err.println("Send OTP error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Không thể gửi mã OTP"));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        try {
            String verificationToken = request.get("verificationToken");
            String otp = request.get("otp");

            // Service handles type detection
            OtpVerificationResult result = authService.verifyOtp(verificationToken, otp);

            // Build response based on type
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("email", result.getEmail());
            response.put("type", result.getType());

            if ("registration".equals(result.getType())) {
                response.put("hashedPassword", result.getHashedPassword());
                response.put("message", "Xác thực OTP thành công");
            } else {
                response.put("resetToken", result.getResetToken());
                response.put("message", "OTP xác thực thành công");
            }

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            System.err.println("Verify OTP error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()));
        } catch (Exception e) {
            System.err.println("Verify OTP error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Lỗi hệ thống. Vui lòng thử lại sau."));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String hashedPassword = request.get("hashedPassword");

            // Register user
            Long userId = authService.registerUser(email, hashedPassword);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "userId", userId,
                    "message", "Đăng ký thành công"));

        } catch (RuntimeException e) {
            System.err.println("Register error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()));
        } catch (Exception e) {
            System.err.println("Register error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Không thể đăng ký tài khoản"));
        }
    }

    // ============ PASSWORD RESET WITH OTP ============

    /**
     * Step 1: Initiate password reset - Send OTP to email
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            String email = request.getEmail();

            if (email == null || email.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new PasswordResetResponse(
                                false, "Email không được để trống"));
            }

            // Initiate password reset via AuthService
            String verificationToken = authService.initiatePasswordReset(email);

            return ResponseEntity.ok(new PasswordResetResponse(
                    true,
                    "Mã OTP đã được gửi đến email của bạn",
                    null,
                    verificationToken));

        } catch (RuntimeException e) {
            System.err.println("Forgot password error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PasswordResetResponse(
                            false, e.getMessage()));
        } catch (Exception e) {
            System.err.println("Forgot password error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PasswordResetResponse(
                            false, "Không thể gửi mã OTP. Vui lòng thử lại sau."));
        }
    }

    /**
     * Step 2: Reset password with reset token
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            String resetToken = request.getResetToken();
            String newPassword = request.getNewPassword();

            authService.resetPassword(resetToken, newPassword);

            return ResponseEntity.ok(new PasswordResetResponse(
                    true,
                    "Đặt lại mật khẩu thành công! Bạn có thể đăng nhập với mật khẩu mới."));

        } catch (RuntimeException e) {
            System.err.println("Reset password error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new PasswordResetResponse(
                            false, e.getMessage()));
        } catch (Exception e) {
            System.err.println("Reset password error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PasswordResetResponse(
                            false, "Không thể đặt lại mật khẩu. Vui lòng thử lại sau."));
        }
    }
}
