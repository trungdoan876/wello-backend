package com.wello.wellobackend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.wello.wellobackend.dto.requests.AuthRequest;
import com.wello.wellobackend.dto.responses.AuthResponse;
import com.wello.wellobackend.enums.AuthProvider;
import com.wello.wellobackend.model.User;
import com.wello.wellobackend.repository.AuthRepository;
import com.wello.wellobackend.repository.TargetRepository;
import com.wello.wellobackend.dto.responses.OtpVerificationResult;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthRepository userRepository;
    private final TargetRepository targetRepository;
    private final GoogleTokenVerifier googleTokenVerifier;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private EmailService emailService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    public AuthServiceImpl(AuthRepository userRepository, TargetRepository targetRepository,
            GoogleTokenVerifier googleTokenVerifier) {
        this.userRepository = userRepository;
        this.targetRepository = targetRepository;
        this.googleTokenVerifier = googleTokenVerifier;
    }

    /**
     * Generate a 6-digit OTP
     */
    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    /**
     * Create verification token (JWT) with email, hashed password, and OTP
     */
    private String createVerificationToken(String email, String password, String otp) {
        // Hash password only if not empty (to distinguish registration vs password
        // reset)
        String hashedPassword = "";
        if (password != null && !password.isEmpty()) {
            hashedPassword = passwordEncoder.encode(password);
        }

        // Create token with 5 minutes expiration
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 5 * 60 * 1000); // 5 minutes

        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(email)
                .claim("hashedPassword", hashedPassword)
                .claim("otp", otp)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Verify token and OTP
     */
    private Map<String, String> verifyToken(String token, String userOtp) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();
            String hashedPassword = claims.get("hashedPassword", String.class);
            String tokenOtp = claims.get("otp", String.class);

            // Verify OTP
            if (!tokenOtp.equals(userOtp)) {
                throw new RuntimeException("Mã OTP không đúng. Vui lòng kiểm tra lại.");
            }

            // Return email and hashed password
            Map<String, String> result = new HashMap<>();
            result.put("email", email);
            result.put("hashedPassword", hashedPassword);
            return result;

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new RuntimeException("Mã OTP đã hết hạn. Vui lòng gửi lại mã OTP mới.");
        } catch (Exception e) {
            throw new RuntimeException("Phiên xác thực không hợp lệ. Vui lòng gửi lại mã OTP.");
        }
    }

    // ============ LOGIN ============

    @Override
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail());

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new AuthResponse(false, "Email hoặc mật khẩu không đúng!", -1, false);
        }

        boolean hasTarget = targetRepository.existsByUser(user);

        return new AuthResponse(
                true,
                "Đăng nhập thành công!",
                user.getIdUser(),
                hasTarget);
    }

    @Override
    public AuthResponse loginWithGoogle(String idToken) throws Exception {
        // 1. Verify token with Google
        GoogleIdToken.Payload payload = googleTokenVerifier.verifyToken(idToken);

        // 2. Extract user info from Google
        String googleId = payload.getSubject();
        String email = payload.getEmail();

        // 3. Find user by Google ID only (separate from email accounts)
        User user = userRepository.findByGoogleId(googleId);

        if (user == null) {
            // Create new user for Google sign-in
            user = new User();
            user.setGoogleId(googleId);
            user.setEmail(email);
            user.setAuthProvider(AuthProvider.GOOGLE);
            userRepository.save(user);
        }

        // 4. Check if user has completed survey
        boolean hasTarget = targetRepository.existsByUser(user);

        return new AuthResponse(
                true,
                "Đăng nhập Google thành công!",
                user.getIdUser(),
                hasTarget);
    }

    // ============ REGISTRATION WITH OTP ============

    @Override
    public String sendRegistrationOtp(String email, String password) {
        // Check if email already exists
        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            throw new RuntimeException("Email đã được đăng ký");
        }

        // Generate OTP
        String otp = generateOtp();

        // Create verification token
        String verificationToken = createVerificationToken(email, password, otp);

        // Send OTP email
        emailService.sendOtpEmail(email, otp);

        return verificationToken;
    }

    @Override
    public OtpVerificationResult verifyOtp(String verificationToken, String otp) {
        // Verify token and OTP
        Map<String, String> tokenData = verifyToken(verificationToken, otp);
        String email = tokenData.get("email");
        String hashedPassword = tokenData.get("hashedPassword");

        // DEBUG: Log hashedPassword value
        System.out.println("🔍 [verifyOtp] Email: " + email);
        System.out.println("🔍 [verifyOtp] HashedPassword: " + hashedPassword);
        System.out.println(
                "🔍 [verifyOtp] HashedPassword isEmpty: " + (hashedPassword == null || hashedPassword.isEmpty()));

        // Detect type based on hashedPassword
        boolean isRegistration = hashedPassword != null && !hashedPassword.isEmpty();

        System.out.println("🔍 [verifyOtp] isRegistration: " + isRegistration);

        if (isRegistration) {
            // Registration flow
            System.out.println("✅ [verifyOtp] Returning REGISTRATION flow");
            return new OtpVerificationResult(email, hashedPassword, null, "registration");
        } else {
            // Password reset flow - generate reset token
            System.out.println("✅ [verifyOtp] Returning PASSWORD_RESET flow");
            String resetToken = generateResetToken(email);
            return new OtpVerificationResult(email, null, resetToken, "password_reset");
        }
    }

    @Override
    public Long registerUser(String email, String hashedPassword) {
        // Create user account
        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword); // Already hashed
        user.setAuthProvider(AuthProvider.EMAIL);

        user = userRepository.save(user);

        return (long) user.getIdUser();
    }

    // ============ PASSWORD RESET WITH OTP ============

    @Override
    public String initiatePasswordReset(String email) {
        // Find user by email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Email không tồn tại trong hệ thống");
        }

        // Check if user is Google OAuth user (they don't have password)
        if (user.getAuthProvider() == AuthProvider.GOOGLE && user.getPassword() == null) {
            throw new RuntimeException("Tài khoản Google không thể đặt lại mật khẩu. Vui lòng đăng nhập bằng Google.");
        }

        // Generate OTP
        String otp = generateOtp();

        // Create verification token (pass empty password for password reset)
        String verificationToken = createVerificationToken(email, "", otp);

        // Send password reset email
        emailService.sendPasswordResetEmail(email, otp);

        return verificationToken;
    }

    @Override
    public String generateResetToken(String email) {
        // Verify user exists
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Email không tồn tại trong hệ thống");
        }

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
            // Parse and verify reset token
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(resetToken)
                    .getBody();

            String email = claims.getSubject();
            String purpose = claims.get("purpose", String.class);

            // Verify purpose
            if (!"password_reset".equals(purpose)) {
                throw new RuntimeException("Token không hợp lệ");
            }

            // Find user
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("Người dùng không tồn tại");
            }

            // Hash and update password
            String hashedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(hashedPassword);
            userRepository.save(user);

        } catch (Exception e) {
            throw new RuntimeException("Token không hợp lệ hoặc đã hết hạn");
        }
    }
}
