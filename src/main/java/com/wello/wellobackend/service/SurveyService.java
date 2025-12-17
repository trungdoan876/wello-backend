package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.requests.SurveyRequest;
import com.wello.wellobackend.dto.responses.QuestionResponse;
import com.wello.wellobackend.dto.responses.TargetResponse;
import com.wello.wellobackend.model.SurveyQuestion;

import java.util.List;

public interface SurveyService {
    List<QuestionResponse> getListSurveyQuestion();
    TargetResponse processSurvey(SurveyRequest surveyRequest);
}
