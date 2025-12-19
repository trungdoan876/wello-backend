package com.wello.wellobackend.controller;

import com.wello.wellobackend.dto.requests.AvatarUploadRequest;
import com.wello.wellobackend.dto.responses.UserInfoResponse;
import com.wello.wellobackend.dto.responses.UserProfileResponse;
import com.wello.wellobackend.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable int userId) {
        try {
            UserProfileResponse response = profileService.getUserProfile(userId);
            if (response == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Profile not found for userId=" + userId);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Get profile error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to get profile");
        }
    }

    @GetMapping("/info/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable int userId) {
        try {
            UserInfoResponse response = profileService.getUserInfo(userId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Get user info error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to get user info");
        }
    }

    @PostMapping("/{userId}/avatar/base64")
    public ResponseEntity<?> uploadAvatarBase64(
            @PathVariable int userId,
            @RequestBody AvatarUploadRequest request) {
        try {
            if (request == null || request.getBase64Image() == null || request.getBase64Image().isBlank()) {
                return ResponseEntity.badRequest().body("Base64 image is required");
            }

            profileService.uploadAvatarBase64(userId, request.getBase64Image());
            return ResponseEntity.ok("Avatar uploaded successfully");
        } catch (RuntimeException e) {
            System.err.println("Upload avatar error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Upload avatar error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload avatar: " + e.getMessage());
        }
    }
}
