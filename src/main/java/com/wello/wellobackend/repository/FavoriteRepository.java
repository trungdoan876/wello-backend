package com.wello.wellobackend.repository;

import com.wello.wellobackend.model.Favorite;
import com.wello.wellobackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    List<Favorite> findByUser(User user);

    List<Favorite> findByUserAndFoodNameContainingIgnoreCase(User user, String foodName);

    void deleteByIdAndUser(int id, User user);
}
