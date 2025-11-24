package com.wello.wellobackend.controller;

import com.wello.wellobackend.dto.requests.RegisterRequest;
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
    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegisterRequest req){
        try {
            // Gọi service để đăng ký
            AuthResponse response = authService.register(req);

            HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(response, status);

        } catch (DataIntegrityViolationException ex) {
            // Bắt lỗi duplicate email từ DB
            return new ResponseEntity<>(
                    new AuthResponse(false, "Email đã tồn tại"),
                    HttpStatus.BAD_REQUEST
            );

        } catch (Exception ex) {
            // Bắt tất cả lỗi khác
            ex.getMessage();
            return new ResponseEntity<>(
                    new AuthResponse(false, "Đăng ký thất bại: " + ex.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
