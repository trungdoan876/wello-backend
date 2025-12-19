package com.wello.wellobackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "survey_progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyProgress implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProgress;

    @OneToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "current_question", nullable = false)
    private SurveyQuestion currentQuestion;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
