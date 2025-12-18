package com.wello.wellobackend.repository;

import com.wello.wellobackend.model.NutritionTracker;
import com.wello.wellobackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NutritionTrackerRepository extends JpaRepository<NutritionTracker, Integer> {

    @Query("SELECT n FROM NutritionTracker n WHERE n.user = :user AND n.date >= :startDate AND n.date < :endDate")
    Optional<NutritionTracker> findByUserAndDate(User user, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT n FROM NutritionTracker n WHERE n.user = :user AND n.date >= :startDate AND n.date < :endDate")
    List<NutritionTracker> findByUserAndDateRange(User user, LocalDateTime startDate, LocalDateTime endDate);
}
