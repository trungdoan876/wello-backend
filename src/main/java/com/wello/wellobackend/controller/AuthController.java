package com.wello.wellobackend.controller;

import com.wello.wellobackend.dto.requests.AuthRequest;
import com.wello.wellobackend.dto.requests.GoogleLoginRequest;
import com.wello.wellobackend.dto.responses.AuthResponse;
import com.wello.wellobackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
        try {
            AuthResponse response = authService.register(request);

            if (!response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Log lỗi để backend biết
            System.err.println("Register error: " + e.getMessage());

            // Trả message chung chung cho client → AN TOÀN
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse(false, "Đăng ký thất bại!", -1, false));
        }
    }

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

    @Autowired
    private com.wello.wellobackend.service.EmailService emailService;

    @Autowired
    private com.wello.wellobackend.service.VerificationTokenService verificationTokenService;

    @Autowired
    private com.wello.wellobackend.repository.AuthRepository authRepository;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody java.util.Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");

            if (email == null || email.isBlank() || password == null || password.isBlank()) {
                return ResponseEntity.badRequest().body(java.util.Map.of("message", "Email and password are required"));
            }

            // Check if email already exists
            com.wello.wellobackend.model.User existingUser = authRepository.findByEmail(email);
            if (existingUser != null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("message", "Email already registered"));
            }

            // Generate 6-digit OTP
            String otp = String.format("%06d", new java.util.Random().nextInt(999999));

            // Create verification token
            String verificationToken = verificationTokenService.createVerificationToken(email, password, otp);

            // Send OTP email
            emailService.sendOtpEmail(email, otp);

            return ResponseEntity.ok(java.util.Map.of(
                    "verificationToken", verificationToken,
                    "message", "OTP sent to email"));

        } catch (Exception e) {
            System.err.println("Send OTP error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("message", "Failed to send OTP"));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody java.util.Map<String, String> request) {
        try {
            String verificationToken = request.get("verificationToken");
            String otp = request.get("otp");

            if (verificationToken == null || otp == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("message", "Token and OTP are required"));
            }

            // Verify token and OTP
            java.util.Map<String, String> tokenData = verificationTokenService.verifyToken(verificationToken, otp);
            String email = tokenData.get("email");
            String hashedPassword = tokenData.get("hashedPassword");

            // Create user account
            com.wello.wellobackend.model.User user = new com.wello.wellobackend.model.User();
            user.setEmail(email);
            user.setPassword(hashedPassword); // Already hashed in token service
            user.setAuthProvider(com.wello.wellobackend.enums.AuthProvider.EMAIL);

            user = authRepository.save(user);

            return ResponseEntity.ok(java.util.Map.of(
                    "userId", user.getIdUser(),
                    "message", "Registration successful"));

        } catch (RuntimeException e) {
            System.err.println("Verify OTP error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(java.util.Map.of("message", e.getMessage()));
        } catch (Exception e) {
            System.err.println("Verify OTP error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("message", "Failed to verify OTP"));
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody java.util.Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");

            if (email == null || email.isBlank() || password == null || password.isBlank()) {
                return ResponseEntity.badRequest().body(java.util.Map.of("message", "Email and password are required"));
            }

            // Check if email already exists
            com.wello.wellobackend.model.User existingUser = authRepository.findByEmail(email);
            if (existingUser != null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("message", "Email already registered"));
            }

            // Generate new 6-digit OTP
            String otp = String.format("%06d", new java.util.Random().nextInt(999999));

            // Create new verification token
            String verificationToken = verificationTokenService.createVerificationToken(email, password, otp);

            // Send OTP email
            emailService.sendOtpEmail(email, otp);

            return ResponseEntity.ok(java.util.Map.of(
                    "verificationToken", verificationToken,
                    "message", "New OTP sent to email"));

        } catch (Exception e) {
            System.err.println("Resend OTP error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(java.util.Map.of("message", "Failed to resend OTP"));
        }
    }
}
