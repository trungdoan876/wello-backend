package com.wello.wellobackend.controller;

import com.wello.wellobackend.dto.requests.AuthRequest;
import com.wello.wellobackend.dto.requests.ForgotPasswordRequest;
import com.wello.wellobackend.dto.requests.GoogleLoginRequest;
import com.wello.wellobackend.dto.requests.ResetPasswordRequest;
import com.wello.wellobackend.dto.requests.VerifyResetOtpRequest;
import com.wello.wellobackend.dto.responses.AuthResponse;
import com.wello.wellobackend.dto.responses.PasswordResetResponse;
import com.wello.wellobackend.service.AuthService;
import com.wello.wellobackend.service.OtpService;
import com.wello.wellobackend.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private OtpService otpService;

    @Autowired
    private PasswordResetService passwordResetService;

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

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");

            // Send OTP via service (includes email existence check)
            String verificationToken = otpService.sendOtpEmail(email, password);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "verificationToken", verificationToken,
                    "message", "OTP sent to email"));

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
                            "message", "Failed to send OTP"));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        try {
            String verificationToken = request.get("verificationToken");
            String otp = request.get("otp");

            // Register user with verified OTP
            Long userId = authService.registerWithOtp(verificationToken, otp);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "userId", userId,
                    "message", "Registration successful"));

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
                            "message", "Failed to verify OTP"));
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");

            // Send OTP via service (includes email existence check)
            String verificationToken = otpService.sendOtpEmail(email, password);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "verificationToken", verificationToken,
                    "message", "New OTP sent to email"));

        } catch (RuntimeException e) {
            System.err.println("Resend OTP error: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()));
        } catch (Exception e) {
            System.err.println("Resend OTP error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Failed to resend OTP"));
        }
    }

    /**
     * Step 1: Initiate password reset - Send OTP to email
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {
        try {
            String email = request.getEmail();

            if (email == null || email.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new PasswordResetResponse(
                                false, "Email không được để trống"));
            }

            // Initiate password reset and send OTP
            String verificationToken = passwordResetService.initiatePasswordReset(email);

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
     * Step 2: Verify OTP and get reset token
     */
    @PostMapping("/verify-reset-otp")
    public ResponseEntity<?> verifyResetOtp(
            @RequestBody VerifyResetOtpRequest request) {
        try {
            String email = request.getEmail();
            String otp = request.getOtp();
            String verificationToken = request.getVerificationToken();

            if (email == null || otp == null || verificationToken == null) {
                return ResponseEntity.badRequest()
                        .body(new PasswordResetResponse(
                                false, "Email, OTP và verification token không được để trống"));
            }

            // Verify OTP and get reset token
            String resetToken = passwordResetService.verifyResetOtp(email, otp, verificationToken);

            return ResponseEntity.ok(new PasswordResetResponse(
                    true,
                    "OTP xác thực thành công",
                    resetToken,
                    null));

        } catch (RuntimeException e) {
            System.err.println("Verify reset OTP error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new com.wello.wellobackend.dto.responses.PasswordResetResponse(
                            false, e.getMessage()));
        } catch (Exception e) {
            System.err.println("Verify reset OTP error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PasswordResetResponse(
                            false, "Không thể xác thực OTP. Vui lòng thử lại sau."));
        }
    }

    /**
     * Step 3: Reset password with reset token
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestBody ResetPasswordRequest request) {
        try {
            String resetToken = request.getResetToken();
            String newPassword = request.getNewPassword();

            if (resetToken == null || newPassword == null || newPassword.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new PasswordResetResponse(
                                false, "Reset token và mật khẩu mới không được để trống"));
            }

            if (newPassword.length() < 6) {
                return ResponseEntity.badRequest()
                        .body(new PasswordResetResponse(
                                false, "Mật khẩu phải có ít nhất 6 ký tự"));
            }

            // Reset password
            passwordResetService.resetPassword(resetToken, newPassword);

            return ResponseEntity.ok(new PasswordResetResponse(
                    true,
                    "Đặt lại mật khẩu thành công! Bạn có thể đăng nhập với mật khẩu mới."));

        } catch (RuntimeException e) {
            System.err.println("Reset password error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new com.wello.wellobackend.dto.responses.PasswordResetResponse(
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
