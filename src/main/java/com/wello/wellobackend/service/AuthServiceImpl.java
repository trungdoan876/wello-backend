package com.wello.wellobackend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.wello.wellobackend.dto.requests.AuthRequest;
import com.wello.wellobackend.dto.responses.AuthResponse;
import com.wello.wellobackend.enums.AuthProvider;
import com.wello.wellobackend.model.User;
import com.wello.wellobackend.repository.AuthRepository;
import com.wello.wellobackend.repository.TargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthRepository userRepository;
    private final TargetRepository targetRepository;
    private final GoogleTokenVerifier googleTokenVerifier;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public AuthServiceImpl(AuthRepository userRepository, TargetRepository targetRepository,
            GoogleTokenVerifier googleTokenVerifier) {
        this.userRepository = userRepository;
        this.targetRepository = targetRepository;
        this.googleTokenVerifier = googleTokenVerifier;
    }

    @Override
    public AuthResponse register(AuthRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            User user = userRepository.findByEmail(request.getEmail());
            return new AuthResponse(false, "Email đã tồn tại!", -1, false);
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setAuthProvider(AuthProvider.EMAIL);

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashedPassword);

        userRepository.save(user);
        return new AuthResponse(true, "Đăng ký thành công!", user.getIdUser(), false);
    }

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
        // Note: name and picture will be captured in Profile during survey

        // 3. Find user by Google ID first, then by email
        User user = userRepository.findByGoogleId(googleId);

        if (user == null) {
            // Try to find by email (in case user registered with email first)
            user = userRepository.findByEmail(email);
        }

        if (user == null) {
            // Create new user for Google sign-in
            user = new User();
            user.setGoogleId(googleId);
            user.setEmail(email);
            user.setAuthProvider(AuthProvider.GOOGLE);
            // No password for Google users
            userRepository.save(user);
        } else {
            // Update existing user with Google ID if not set
            if (user.getGoogleId() == null) {
                user.setGoogleId(googleId);
                userRepository.save(user);
            }
        }

        // 4. Check if user has completed survey
        boolean hasTarget = targetRepository.existsByUser(user);

        return new AuthResponse(
                true,
                "Đăng nhập Google thành công!",
                user.getIdUser(),
                hasTarget);
    }

}
