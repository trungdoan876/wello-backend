package com.wello.wellobackend.controller;

import com.wello.wellobackend.dto.requests.AvatarUploadRequest;
import com.wello.wellobackend.dto.responses.UserInfoResponse;
import com.wello.wellobackend.dto.responses.UserProfileResponse;
import com.wello.wellobackend.service.NotificationSettingsService;
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

    @Autowired
    private NotificationSettingsService notificationSettingsService;

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

    @PostMapping("/{userId}/fcm-token")
    public ResponseEntity<?> updateFcmToken(@PathVariable int userId, @RequestParam String fcmToken) {
        try {
            profileService.updateFcmToken(userId, fcmToken);
            return ResponseEntity.ok("FCM token updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update FCM token");
        }
    }

    @PostMapping("/{userId}/water-reminder-settings")
    public ResponseEntity<?> updateWaterReminderSettings(
            @PathVariable int userId,
            @RequestParam boolean enabled,
            @RequestParam int startHour,
            @RequestParam int endHour,
            @RequestParam(defaultValue = "1") int intervalHours,
            @RequestParam(defaultValue = "0") int intervalMinutes) {
        try {
            notificationSettingsService.updateWaterReminderSettings(userId, enabled, startHour, endHour, intervalHours,
                    intervalMinutes);
            return ResponseEntity.ok("Water reminder settings updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update reminder settings");
        }
    }

    @GetMapping("/test-noti/{userId}")
    public ResponseEntity<?> testNotification(@PathVariable int userId) {
        try {
            UserInfoResponse user = profileService.getUserInfo(userId);
            String token = user.getFcmToken();
            if (token == null || token.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("User does not have an FCM token. Please update it first via /fcm-token endpoint.");
            }
            profileService.testPushNotification(userId);
            return ResponseEntity.ok("Test notification sent to user " + userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}/water-reminder-settings")
    public ResponseEntity<?> getWaterReminderSettings(@PathVariable int userId) {
        try {
            // This will be implemented in NotificationSettingsService
            return ResponseEntity.ok("Feature to get settings - to be implemented");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
