package com.wello.wellobackend.controller;

import com.wello.wellobackend.dto.requests.AddFavoriteComboRequest;
import com.wello.wellobackend.dto.requests.LogFavoriteRequest;
import com.wello.wellobackend.dto.requests.UpdateFavoriteComboRequest;
import com.wello.wellobackend.dto.responses.FavoriteComboResponse;
import com.wello.wellobackend.dto.responses.LogFoodResponse;
import com.wello.wellobackend.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/my-favorites")
    public ResponseEntity<List<FavoriteComboResponse>> getMyFavorites(
            @RequestParam int userId) {
        return ResponseEntity.ok(favoriteService.getFavoritesByUser(userId));
    }

    @GetMapping("/{favoriteId}")
    public ResponseEntity<FavoriteComboResponse> getFavoriteById(
            @RequestParam int userId,
            @PathVariable int favoriteId) {
        return ResponseEntity.ok(favoriteService.getFavoriteById(userId, favoriteId));
    }

    @PostMapping("/add-combo")
    public ResponseEntity<FavoriteComboResponse> addFavoriteCombo(
            @RequestBody AddFavoriteComboRequest request) {
        return ResponseEntity.ok(favoriteService.addFavoriteCombo(request));
    }

    @PutMapping("/update-combo")
    public ResponseEntity<FavoriteComboResponse> updateFavoriteCombo(
            @RequestBody UpdateFavoriteComboRequest request) {
        return ResponseEntity.ok(favoriteService.updateFavoriteCombo(request));
    }

    @PostMapping("/log")
    public ResponseEntity<LogFoodResponse> logFavorite(
            @RequestBody LogFavoriteRequest request) {
        return ResponseEntity.ok(favoriteService.logFavorite(request));
    }

    @DeleteMapping("/delete/{favoriteId}")
    public ResponseEntity<String> deleteFavorite(
            @RequestParam int userId,
            @PathVariable int favoriteId) {
        favoriteService.deleteFavorite(userId, favoriteId);
        return ResponseEntity.ok("Đã xóa khỏi yêu thích");
    }

    @GetMapping("/search")
    public ResponseEntity<List<FavoriteComboResponse>> searchFavorites(
            @RequestParam int userId,
            @RequestParam String query) {
        return ResponseEntity.ok(favoriteService.searchFavorites(userId, query));
    }
}
