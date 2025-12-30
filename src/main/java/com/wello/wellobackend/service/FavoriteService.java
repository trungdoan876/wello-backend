package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.requests.AddCustomFoodRequest;
import com.wello.wellobackend.dto.requests.AddFavoriteFoodRequest;
import com.wello.wellobackend.dto.requests.LogFavoriteRequest;
import com.wello.wellobackend.dto.requests.UpdateCustomFoodRequest;
import com.wello.wellobackend.dto.requests.UpdateFavoriteFoodRequest;
import com.wello.wellobackend.dto.responses.CustomFoodResponse;
import com.wello.wellobackend.dto.responses.FavoriteFoodDetailResponse;
import com.wello.wellobackend.dto.responses.LogFoodResponse;

import java.util.List;

public interface FavoriteService {
    List<FavoriteFoodDetailResponse> getFavoritesByUser(int userId);

    FavoriteFoodDetailResponse getFavoriteById(int userId, int favoriteId);

    FavoriteFoodDetailResponse addFavoriteFood(AddFavoriteFoodRequest request);

    FavoriteFoodDetailResponse updateFavoriteFood(UpdateFavoriteFoodRequest request);

    CustomFoodResponse addCustomFood(AddCustomFoodRequest request);

    CustomFoodResponse updateCustomFood(UpdateCustomFoodRequest request);

    void deleteFavorite(int userId, int favoriteId);

    List<FavoriteFoodDetailResponse> searchFavorites(int userId, String query);

    LogFoodResponse logFavorite(LogFavoriteRequest request);
}
