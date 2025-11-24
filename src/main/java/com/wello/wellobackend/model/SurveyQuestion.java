package com.wello.wellobackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "question")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyQuestion {
    @Id
    @Column(name = "id_question")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idQuestion;

    @Column(name = "question",nullable = false)
    private String question;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<SurveyAnswer> surveyAnswers;

}
