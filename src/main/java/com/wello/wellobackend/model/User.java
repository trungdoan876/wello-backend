package com.wello.wellobackend.model;

import com.wello.wellobackend.enums.AuthProvider;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private int idUser;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password") // Nullable for Google users
    private String password;

    // Google OAuth fields
    @Column(name = "google_id", unique = true)
    private String googleId;

    @Column(name = "auth_provider")
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @Column(name = "fcm_token")
    private String fcmToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Target> targets;

    // mappedBy = "user"
    // cascade = CascadeType.ALL: Lưu User -> tự lưu Tracker. Xóa User -> tự xóa
    // Tracker.
    // orphanRemoval = true: Nếu setUser(null) -> Xóa dòng Tracker trong DB.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HealthTracker> healthTrackers;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private SurveyProgress surveyProgress;
}
