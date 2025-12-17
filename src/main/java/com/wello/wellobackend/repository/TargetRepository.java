package com.wello.wellobackend.repository;

import com.wello.wellobackend.model.Target;
import com.wello.wellobackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TargetRepository extends JpaRepository<Target, Integer> {
    boolean existsByUser(User user);

    Target findByUser(User user);
}
