package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.requests.AddFavoriteRequest;
import com.wello.wellobackend.dto.responses.FavoriteResponse;

import java.util.List;

public interface FavoriteService {
    List<FavoriteResponse> getFavoritesByUser(int userId);

    FavoriteResponse addFavorite(AddFavoriteRequest request);

    void deleteFavorite(int userId, int favoriteId);

    List<FavoriteResponse> searchFavorites(int userId, String query);
}
