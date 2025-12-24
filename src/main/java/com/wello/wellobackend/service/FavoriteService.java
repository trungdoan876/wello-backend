package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.requests.AddFavoriteComboRequest;
import com.wello.wellobackend.dto.requests.LogFavoriteRequest;
import com.wello.wellobackend.dto.requests.UpdateFavoriteComboRequest;
import com.wello.wellobackend.dto.responses.FavoriteComboResponse;
import com.wello.wellobackend.dto.responses.LogFoodResponse;

import java.util.List;

public interface FavoriteService {
    List<FavoriteComboResponse> getFavoritesByUser(int userId);

    FavoriteComboResponse getFavoriteById(int userId, int favoriteId);

    FavoriteComboResponse addFavoriteCombo(AddFavoriteComboRequest request);

    FavoriteComboResponse updateFavoriteCombo(UpdateFavoriteComboRequest request);

    void deleteFavorite(int userId, int favoriteId);

    List<FavoriteComboResponse> searchFavorites(int userId, String query);

    LogFoodResponse logFavorite(LogFavoriteRequest request);
}
