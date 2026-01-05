package com.wello.wellobackend.repository;

import com.wello.wellobackend.model.DailyStreak;
import com.wello.wellobackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyStreakRepository extends JpaRepository<DailyStreak, Integer> {

    Optional<DailyStreak> findByUserAndDate(User user, LocalDate date);

    List<DailyStreak> findAllByUserAndDateBetween(
        User user,
        LocalDate start,
        LocalDate end
    );
}
