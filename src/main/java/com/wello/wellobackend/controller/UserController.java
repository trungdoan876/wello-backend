package com.wello.wellobackend.controller;

import com.wello.wellobackend.dto.responses.UserProfileResponse;
import com.wello.wellobackend.dto.responses.UserInfoResponse;
import com.wello.wellobackend.dto.responses.UserVerificationResponse;
import com.wello.wellobackend.model.History;
import com.wello.wellobackend.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@RequestParam int userId) {
        return ResponseEntity.ok(profileService.getUserProfile(userId));
    }

    @GetMapping("/verify")
    public ResponseEntity<UserVerificationResponse> verifyUser(
            @RequestParam int userId) {
        UserVerificationResponse response = profileService.verifyUser(userId);
        if (response.isExists()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body(response);
        }
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> getUserInfo(@RequestParam int userId) {
        return ResponseEntity.ok(profileService.getUserInfo(userId));
    }

    @GetMapping("/profile/history")
    public ResponseEntity<List<History>> getProfileHistory(
            @RequestParam int userId) {
        return ResponseEntity.ok(profileService.getProfileHistory(userId));
    }
}
