package com.wello.wellobackend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "answer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyAnswer {
    @Id
    @Column(name = "id_answer")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAnswer;

    @Column(name = "answer")
    private String answer;

    @ManyToOne
    @JoinColumn(name = "id_question")
    private SurveyQuestion question;
}

