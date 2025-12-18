package com.wello.wellobackend.controller;

import com.wello.wellobackend.dto.responses.UserProfileResponse;
import com.wello.wellobackend.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<com.wello.wellobackend.dto.responses.UserVerificationResponse> verifyUser(
            @RequestParam int userId) {
        com.wello.wellobackend.dto.responses.UserVerificationResponse response = profileService.verifyUser(userId);
        if (response.isExists()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body(response);
        }
    }
}
