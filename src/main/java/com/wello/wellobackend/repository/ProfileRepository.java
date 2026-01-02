package com.wello.wellobackend.repository;

import com.wello.wellobackend.model.Profile;
import com.wello.wellobackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    Profile findByUser(User user);

    Profile findByUser_IdUser(int userId);
}
