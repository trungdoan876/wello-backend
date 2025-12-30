package com.wello.wellobackend.controller;

import com.wello.wellobackend.dto.requests.AddCustomFoodRequest;
import com.wello.wellobackend.dto.requests.AddFavoriteFoodRequest;
import com.wello.wellobackend.dto.requests.LogFavoriteRequest;
import com.wello.wellobackend.dto.requests.UpdateCustomFoodRequest;
import com.wello.wellobackend.dto.requests.UpdateFavoriteFoodRequest;
import com.wello.wellobackend.dto.responses.CustomFoodResponse;
import com.wello.wellobackend.dto.responses.FavoriteFoodDetailResponse;
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
    public ResponseEntity<List<FavoriteFoodDetailResponse>> getMyFavorites(
            @RequestParam int userId) {
        return ResponseEntity.ok(favoriteService.getFavoritesByUser(userId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FavoriteFoodDetailResponse>> getFavoritesByUserId(
            @PathVariable int userId) {
        return ResponseEntity.ok(favoriteService.getFavoritesByUser(userId));
    }

    @GetMapping("/{favoriteId}")
    public ResponseEntity<FavoriteFoodDetailResponse> getFavoriteById(
            @RequestParam int userId,
            @PathVariable int favoriteId) {
        return ResponseEntity.ok(favoriteService.getFavoriteById(userId, favoriteId));
    }

    @PostMapping("/add-favorite-food")
    public ResponseEntity<FavoriteFoodDetailResponse> addFavoriteFood(
            @RequestBody AddFavoriteFoodRequest request) {
        return ResponseEntity.ok(favoriteService.addFavoriteFood(request));
    }

    @PutMapping("/update-favorite-food")
    public ResponseEntity<FavoriteFoodDetailResponse> updateFavoriteFood(
            @RequestBody UpdateFavoriteFoodRequest request) {
        return ResponseEntity.ok(favoriteService.updateFavoriteFood(request));
    }

    @PostMapping("/add-custom-food")
    public ResponseEntity<CustomFoodResponse> addCustomFood(
            @RequestBody AddCustomFoodRequest request) {
        return ResponseEntity.ok(favoriteService.addCustomFood(request));
    }

    @PutMapping("/update-custom-food")
    public ResponseEntity<CustomFoodResponse> updateCustomFood(
            @RequestBody UpdateCustomFoodRequest request) {
        return ResponseEntity.ok(favoriteService.updateCustomFood(request));
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
    public ResponseEntity<List<FavoriteFoodDetailResponse>> searchFavorites(
            @RequestParam int userId,
            @RequestParam String query) {
        return ResponseEntity.ok(favoriteService.searchFavorites(userId, query));
    }
}
