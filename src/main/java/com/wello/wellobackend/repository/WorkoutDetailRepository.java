package com.wello.wellobackend.repository;

import com.wello.wellobackend.model.WorkoutDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutDetailRepository extends JpaRepository<WorkoutDetail, Integer> {
}
