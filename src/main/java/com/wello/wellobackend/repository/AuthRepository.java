package com.wello.wellobackend.repository;

import com.wello.wellobackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    User findByEmail(String email);

    User findByGoogleId(String googleId);

    boolean existsByIdUserAndEmailIgnoreCase(int idUser, String email);
}
