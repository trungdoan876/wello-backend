package com.wello.wellobackend.repository;

import com.wello.wellobackend.model.Favorite;
import com.wello.wellobackend.model.FavoriteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FavoriteItemRepository extends JpaRepository<FavoriteItem, Integer> {
    List<FavoriteItem> findByFavorite(Favorite favorite);

    @Transactional
    @Modifying
    void deleteByFavorite(Favorite favorite);
}
