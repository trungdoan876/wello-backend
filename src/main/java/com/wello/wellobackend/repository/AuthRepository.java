package com.wello.wellobackend.repository;

import com.wello.wellobackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
}
