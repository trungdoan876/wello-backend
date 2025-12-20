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

    // lay danh sach cau hoi de hien thi len man hinh survey -> khao sat
    @GetMapping("/questions")
    public ResponseEntity<List<QuestionResponse>> getQuestions() {
        return ResponseEntity.ok(surveyService.getListSurveyQuestion());
    }

    // nhan ket qua tu fe va tinh toan luong target
    @PostMapping("/submit")
    public ResponseEntity<TargetResponse> submitSurvey(@RequestBody SurveyRequest request) {
        TargetResponse result = surveyService.processSurvey(request);
        return ResponseEntity.ok(result);
    }

    @Autowired
    private com.wello.wellobackend.service.BmiCalculationService bmiCalculationService;

    @PostMapping("/calculate-bmi")
    public ResponseEntity<?> calculateBmi(
            @RequestBody com.wello.wellobackend.dto.requests.BmiCalculationRequest request) {
        try {
            if (request.getWeight() <= 0 || request.getHeight() <= 0) {
                return ResponseEntity.badRequest().body(java.util.Map.of("message", "Invalid weight or height"));
            }

            double bmi = bmiCalculationService.calculateBMI(request.getWeight(), request.getHeight());
            String status = bmiCalculationService.getBMIStatus(bmi);
            String statusText = bmiCalculationService.getBMIStatusText(status);

            // Calculate warning if goal is provided
            String warning = null;
            if (request.getGoal() != null) {
                warning = bmiCalculationService.getGoalWarning(bmi, request.getGoal());
            }

            com.wello.wellobackend.dto.responses.BmiCalculationResponse response = new com.wello.wellobackend.dto.responses.BmiCalculationResponse(
                    bmi, status, statusText, warning);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(java.util.Map.of("message", "Failed to calculate BMI"));
        }
    }

}
