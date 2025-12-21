package com.wello.wellobackend.controller;

import com.wello.wellobackend.dto.requests.AddFavoriteRequest;
import com.wello.wellobackend.dto.responses.FavoriteResponse;
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
    public ResponseEntity<List<FavoriteResponse>> getMyFavorites(
            @RequestParam int userId) {
        return ResponseEntity.ok(favoriteService.getFavoritesByUser(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<FavoriteResponse> addFavorite(
            @RequestBody AddFavoriteRequest request) {
        return ResponseEntity.ok(favoriteService.addFavorite(request));
    }

    @DeleteMapping("/delete/{favoriteId}")
    public ResponseEntity<String> deleteFavorite(
            @RequestParam int userId,
            @PathVariable int favoriteId) {
        favoriteService.deleteFavorite(userId, favoriteId);
        return ResponseEntity.ok("Đã xóa khỏi yêu thích");
    }

    @GetMapping("/search")
    public ResponseEntity<List<FavoriteResponse>> searchFavorites(
            @RequestParam int userId,
            @RequestParam String query) {
        return ResponseEntity.ok(favoriteService.searchFavorites(userId, query));
    }
}
