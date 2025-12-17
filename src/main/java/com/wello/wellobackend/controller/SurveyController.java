package com.wello.wellobackend.controller;

import com.wello.wellobackend.dto.requests.SurveyRequest;
import com.wello.wellobackend.dto.responses.QuestionResponse;
import com.wello.wellobackend.dto.responses.TargetResponse;
import com.wello.wellobackend.model.SurveyQuestion;
import com.wello.wellobackend.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/survey")
public class SurveyController {
    @Autowired
    private SurveyService surveyService;

    //lay danh sach cau hoi de hien thi len man hinh survey -> khao sat
    @GetMapping("/questions")
    public ResponseEntity<List<QuestionResponse>> getQuestions() {
        return ResponseEntity.ok(surveyService.getListSurveyQuestion());
    }
    
    //nhan ket qua tu fe va tinh toan luong target
    @PostMapping("/submit")
    public ResponseEntity<TargetResponse> submitSurvey(@RequestBody SurveyRequest request) {
        TargetResponse result = surveyService.processSurvey(request);
        return ResponseEntity.ok(result);
    }

}
