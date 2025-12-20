package com.wello.wellobackend.controller;

import com.wello.wellobackend.dto.requests.*;
import com.wello.wellobackend.dto.responses.UserInfoResponse;
import com.wello.wellobackend.dto.responses.UserProfileResponse;
import com.wello.wellobackend.service.NotificationSettingsService;
import com.wello.wellobackend.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PutMapping("/{userId}/fullname")
    public ResponseEntity<?> updateFullname(
            @PathVariable int userId,
            @RequestBody UpdateFullnameRequest request) {
        try {
            if (request == null || request.getFullname() == null || request.getFullname().isBlank()) {
                return ResponseEntity.badRequest().body("Fullname is required");
            }

            profileService.updateFullname(userId, request.getFullname());
            return ResponseEntity.ok("Fullname updated successfully");
        } catch (RuntimeException e) {
            System.err.println("Update fullname error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Update fullname error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update fullname: " + e.getMessage());
        }
    }

    @PutMapping("/{userId}/gender")
    public ResponseEntity<?> updateGender(
            @PathVariable int userId,
            @RequestBody UpdateGenderRequest request) {
        try {
            if (request == null || request.getGender() == null) {
                return ResponseEntity.badRequest().body("Gender is required");
            }

            profileService.updateGender(userId, request.getGender());
            return ResponseEntity.ok("Gender updated successfully");
        } catch (RuntimeException e) {
            System.err.println("Update gender error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Update gender error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update gender: " + e.getMessage());
        }
    }

    @PutMapping("/{userId}/age")
    public ResponseEntity<?> updateAge(
            @PathVariable int userId,
            @RequestBody UpdateAgeRequest request) {
        try {
            if (request == null || request.getAge() <= 0) {
                return ResponseEntity.badRequest().body("Valid age is required");
            }

            profileService.updateAge(userId, request.getAge());
            return ResponseEntity.ok("Age updated successfully");
        } catch (RuntimeException e) {
            System.err.println("Update age error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Update age error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update age: " + e.getMessage());
        }
    }

    @PutMapping("/{userId}/height")
    public ResponseEntity<?> updateHeight(
            @PathVariable int userId,
            @RequestBody UpdateHeightRequest request) {
        try {
            if (request == null || request.getHeight() <= 0) {
                return ResponseEntity.badRequest().body("Valid height is required");
            }

            profileService.updateHeight(userId, request.getHeight());
            return ResponseEntity.ok("Height updated successfully");
        } catch (RuntimeException e) {
            System.err.println("Update height error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Update height error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update height: " + e.getMessage());
        }
    }

    @PutMapping("/{userId}/weight")
    public ResponseEntity<?> updateWeight(
            @PathVariable int userId,
            @RequestBody UpdateWeightRequest request) {
        try {
            if (request == null || request.getWeight() <= 0) {
                return ResponseEntity.badRequest().body("Valid weight is required");
            }

            profileService.updateWeight(userId, request.getWeight());
            return ResponseEntity.ok("Weight updated successfully");
        } catch (RuntimeException e) {
            System.err.println("Update weight error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Update weight error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update weight: " + e.getMessage());
        }
    }

    @PutMapping("/{userId}/goal")
    public ResponseEntity<?> updateGoal(
            @PathVariable int userId,
            @RequestBody UpdateGoalRequest request) {
        try {
            if (request == null || request.getGoal() == null) {
                return ResponseEntity.badRequest().body("Goal is required");
            }

            profileService.updateGoal(userId, request.getGoal());
            return ResponseEntity.ok("Goal updated successfully");
        } catch (RuntimeException e) {
            System.err.println("Update goal error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Update goal error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update goal: " + e.getMessage());
        }
    }

    @PutMapping("/{userId}/activity-level")
    public ResponseEntity<?> updateActivityLevel(
            @PathVariable int userId,
            @RequestBody UpdateActivityLevelRequest request) {
        try {
            if (request == null || request.getActivityLevel() == null) {
                return ResponseEntity.badRequest().body("Activity level is required");
            }

            profileService.updateActivityLevel(userId, request.getActivityLevel());
            return ResponseEntity.ok("Activity level updated successfully");
        } catch (RuntimeException e) {
            System.err.println("Update activity level error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Update activity level error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update activity level: " + e.getMessage());
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
            @RequestParam int interval) {
        try {
            notificationSettingsService.updateWaterReminderSettings(userId, enabled, startHour, endHour, interval);
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
}
