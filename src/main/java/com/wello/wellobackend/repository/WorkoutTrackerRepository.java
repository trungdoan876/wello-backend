package com.wello.wellobackend.repository;

import com.wello.wellobackend.model.WorkoutTracker;
import com.wello.wellobackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutTrackerRepository extends JpaRepository<WorkoutTracker, Integer> {
    @Query("SELECT w FROM WorkoutTracker w WHERE w.user = :user AND w.date BETWEEN :start AND :end")
    Optional<WorkoutTracker> findByUserAndDate(User user, LocalDateTime start, LocalDateTime end);

    @Query("SELECT w FROM WorkoutTracker w WHERE w.user = :user AND w.date >= :startDate AND w.date <= :endDate ORDER BY w.date")
    List<WorkoutTracker> findByUserAndDateRange(User user, LocalDateTime startDate, LocalDateTime endDate);
}
