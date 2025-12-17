package com.wello.wellobackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "question")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyQuestion implements Serializable {
    @Id
    @Column(name = "id_question")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idQuestion;

    @Column(name = "question",nullable = false)
    private String question;

    @Column(name = "question_type", nullable = false)
    private String questionType;

    // QUAN TRỌNG: Key để định danh logic (AGE, WEIGHT, HEIGHT...)
    // Flutter sẽ nhìn cái này để biết nên hiện chữ "kg" hay "cm"
    @Column(name = "question_key", length = 50)
    private String questionKey;

    // Dùng cho Slider/Number: Đơn vị (cm, kg)
    @Column(name = "unit_label")
    private String unitLabel;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<SurveyAnswer> surveyAnswers;

    @OneToMany(mappedBy = "currentQuestion")
    private List<SurveyProgress> surveyProgressList;

}
