package com.wello.wellobackend.repository;

import com.wello.wellobackend.model.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion,Integer> {
}
