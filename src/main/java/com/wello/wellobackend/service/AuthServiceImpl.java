package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.requests.RegisterRequest;
import com.wello.wellobackend.dto.responses.AuthResponse;
import com.wello.wellobackend.model.User;
import com.wello.wellobackend.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    public AuthServiceImpl(AuthRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse(false, "Email đã tồn tại!");
        }
        User user = new User();
        user.setEmail(request.getEmail());

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashedPassword);

        userRepository.save(user);
        return new AuthResponse(true,"Đăng ký thành công!");
    }
}
