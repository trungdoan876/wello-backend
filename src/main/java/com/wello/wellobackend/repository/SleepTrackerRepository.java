package com.wello.wellobackend.repository;

import com.wello.wellobackend.model.SleepTracker;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SleepTrackerRepository extends JpaRepository<SleepTracker, Integer> {

        /**
         * Tìm sleep trackers theo user và trong khoảng thời gian
         */
        @Query("SELECT st FROM SleepTracker st WHERE st.user.id = :userId " +
                        "AND st.date BETWEEN :startDate AND :endDate " +
                        "ORDER BY st.date DESC")
        List<SleepTracker> findByUserIdAndDateBetween(
                        @Param("userId") Long userId,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        /**
         * Tìm sleep trackers theo user, sắp xếp theo ngày mới nhất
         */
        @Query("SELECT st FROM SleepTracker st WHERE st.user.id = :userId " +
                        "ORDER BY st.date DESC")
        List<SleepTracker> findByUserIdOrderByDateDesc(@Param("userId") Long userId, Pageable pageable);

        /**
         * Tìm sleep tracker theo ID và userId (để authorization)
         */
        @Query("SELECT st FROM SleepTracker st WHERE st.id = :id AND st.user.id = :userId")
        SleepTracker findByIdAndUserId(
                        @Param("id") int id,
                        @Param("userId") Long userId);

        /**
         * Tìm sleep tracker theo userId và ngày cụ thể
         */
        @Query("SELECT st FROM SleepTracker st WHERE st.user.id = :userId " +
                        "AND DATE(st.date) = :date")
        SleepTracker findByUserIdAndDate(
                        @Param("userId") Long userId,
                        @Param("date") LocalDate date);

        /**
         * Kiểm tra có sleep tracker nào cho ngày này chưa
         */
        @Query("SELECT CASE WHEN COUNT(st) > 0 THEN true ELSE false END " +
                        "FROM SleepTracker st WHERE st.user.id = :userId " +
                        "AND DATE(st.date) = :date")
        boolean existsByUserIdAndDate(
                        @Param("userId") Long userId,
                        @Param("date") LocalDate date);

        // ========== Quick Log Methods ==========

        /**
         * Find PENDING sleep record for user (only 1 PENDING allowed at a time)
         */
        @Query("SELECT st FROM SleepTracker st WHERE st.user.idUser = :userId AND st.status = :status")
        SleepTracker findByUserIdAndStatus(@Param("userId") Long userId,
                        @Param("status") com.wello.wellobackend.enums.SleepStatus status);

        /**
         * Find sleep record by exact date (for GET /api/sleep/today)
         */
        @Query("SELECT st FROM SleepTracker st WHERE st.user.idUser = :userId AND DATE(st.date) = :date")
        SleepTracker findByUserIdAndDateEquals(@Param("userId") Long userId, @Param("date") LocalDate date);
}
