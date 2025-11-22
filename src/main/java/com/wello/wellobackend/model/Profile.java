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

    @Column(name = "full_name")
    private String fullname;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "age")
    private int age;

    @Column(name = "height")
    private int height;

    @Column(name="weight")
    private int weight;

    @Column(name="target_goal")
    @Enumerated(EnumType.STRING)
    private Goal goal;

    @Column(name="activity_level")
    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;

    @Column(name = "survey_date")
    private LocalDateTime surveyDate;

    @OneToOne
    @JoinColumn(name = "id_user")
    private User user;
}
