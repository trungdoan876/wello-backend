package com.wello.wellobackend.controller;

import com.wello.wellobackend.dto.requests.BmiCalculationRequest;
import com.wello.wellobackend.dto.requests.SurveyRequest;
import com.wello.wellobackend.dto.responses.BmiCalculationResponse;
import com.wello.wellobackend.dto.responses.QuestionResponse;
import com.wello.wellobackend.dto.responses.TargetResponse;
import com.wello.wellobackend.service.BmiCalculationService;
import com.wello.wellobackend.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/survey")
public class SurveyController {
    @Autowired
    private SurveyService surveyService;

    @Autowired
    private BmiCalculationService bmiCalculationService;

    // lay danh sach cau hoi de hien thi len man hinh survey -> khao sat
    @GetMapping("/questions")
    public ResponseEntity<List<QuestionResponse>> getQuestions() {
        return ResponseEntity.ok(surveyService.getListSurveyQuestion());
    }

    // nhan ket qua tu fe va tinh toan luong target
    @PostMapping("/submit")
    public ResponseEntity<TargetResponse> submitSurvey(@RequestBody SurveyRequest request) {
        // DEBUG: Log request từ frontend
        System.out.println("========== SURVEY REQUEST FROM FRONTEND ==========");
        System.out.println("userId: " + request.getUserId());
        System.out.println("fullname: " + request.getFullname());
        System.out.println("gender: " + request.getGender());
        System.out.println("age: " + request.getAge());
        System.out.println("height: " + request.getHeight());
        System.out.println("weight: " + request.getWeight());
        System.out.println("goal: " + request.getGoal());
        System.out.println("activityLevel: " + request.getActivityLevel());
        System.out.println("==================================================");

        TargetResponse result = surveyService.processSurvey(request);

        // DEBUG: Log response gửi về frontend
        System.out.println("========== SURVEY RESPONSE TO FRONTEND ==========");
        System.out.println("sleepTargetHours: " + result.getSleepTargetHours());
        System.out.println("sleepBedtimeTarget: " + result.getSleepBedtimeTarget());
        System.out.println("sleepWakeTimeTarget: " + result.getSleepWakeTimeTarget());
        System.out.println("=================================================");

        return ResponseEntity.ok(result);
    }

    @PostMapping("/calculate-bmi")
    public ResponseEntity<?> calculateBmi(@RequestBody BmiCalculationRequest request) {
        try {
            double bmi = bmiCalculationService.calculateBMI(request.getWeight(), request.getHeight());
            String status = bmiCalculationService.getBMIStatus(bmi);
            String statusText = bmiCalculationService.getBMIStatusText(status);

            // Calculate warning if goal is provided
            String warning = null;
            if (request.getGoal() != null) {
                warning = bmiCalculationService.getGoalWarning(bmi, request.getGoal());
            }

            BmiCalculationResponse response = new BmiCalculationResponse(
                    bmi, status, statusText, warning);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Failed to calculate BMI"));
        }
    }

}
