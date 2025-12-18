package com.wello.wellobackend.repository;

import com.wello.wellobackend.model.User;
import com.wello.wellobackend.model.WaterTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WaterTrackerRepository extends JpaRepository<WaterTracker, Integer> {

    @Query("SELECT w FROM WaterTracker w WHERE w.user = :user AND w.date >= :startDate AND w.date < :endDate")
    Optional<WaterTracker> findByUserAndDate(User user, LocalDateTime startDate, LocalDateTime endDate);
}
