package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.requests.AddFavoriteRequest;
import com.wello.wellobackend.dto.responses.FavoriteResponse;
import com.wello.wellobackend.model.Favorite;
import com.wello.wellobackend.model.User;
import com.wello.wellobackend.repository.AuthRepository;
import com.wello.wellobackend.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {

        @Autowired
        private FavoriteRepository favoriteRepository;

        @Autowired
        private AuthRepository authRepository;

        @Override
        public List<FavoriteResponse> getFavoritesByUser(int userId) {
                User user = authRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

                return favoriteRepository.findByUser(user).stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public FavoriteResponse addFavorite(AddFavoriteRequest request) {
                User user = authRepository.findById(request.getUserId())
                                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

                Favorite favorite = new Favorite();
                favorite.setUser(user);
                favorite.setFoodName(request.getFoodName());
                favorite.setCaloriesPer100g(request.getCaloriesPer100g());
                favorite.setProteinPer100g(request.getProteinPer100g());
                favorite.setCarbsPer100g(request.getCarbsPer100g());
                favorite.setFatPer100g(request.getFatPer100g());
                favorite.setMealType(request.getMealType());

                Favorite savedFavorite = favoriteRepository.save(favorite);
                return mapToResponse(savedFavorite);
        }

        @Override
        public void deleteFavorite(int userId, int favoriteId) {
                User user = authRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

                favoriteRepository.deleteByIdAndUser(favoriteId, user);
        }

        @Override
        public List<FavoriteResponse> searchFavorites(int userId, String query) {
                User user = authRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

                return favoriteRepository.findByUserAndFoodNameContainingIgnoreCase(user, query).stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        private FavoriteResponse mapToResponse(Favorite favorite) {
                return FavoriteResponse.builder()
                                .id(favorite.getId())
                                .foodName(favorite.getFoodName())
                                .calories(favorite.getCaloriesPer100g())
                                .protein(favorite.getProteinPer100g())
                                .carbs(favorite.getCarbsPer100g())
                                .fat(favorite.getFatPer100g())
                                .mealType(favorite.getMealType())
                                .build();
        }
}
