package com.wello.wellobackend.model;

import com.wello.wellobackend.enums.ActivityLevel;
import com.wello.wellobackend.enums.Gender;
import com.wello.wellobackend.enums.Goal;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    @Column(name = "id_profile")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProfile;

    @Column(name = "full_name", nullable = false)
    private String fullname;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "height", nullable = false)
    private int height;

    @Column(name="weight", nullable = false)
    private int weight;

    @Column(name="target_goal", nullable = false)
    @Enumerated(EnumType.STRING)
    private Goal goal;

    @Column(name="activity_level",nullable = false)
    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;

    @Column(name = "survey_date", nullable = false)
    private LocalDateTime surveyDate;

    @OneToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;
}
