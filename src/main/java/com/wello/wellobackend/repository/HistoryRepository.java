package com.wello.wellobackend.repository;

import com.wello.wellobackend.model.History;
import com.wello.wellobackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {
    List<History> findByUserOrderByRecordedAtDesc(User user);
}
