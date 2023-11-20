package com.cas.challengeservice.controller;

import com.cas.challengeservice.dto.*;
import com.cas.challengeservice.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/challenge")
public class ChallengeController {
    private final ChallengeService challengeService;

    @Autowired
    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @PostMapping("/start")
    public ResponseEntity<GenericMessage<ChallengeDto>> startChallenge(
            @RequestBody ChallengeStartRequest request) {
        com.cas.challengeservice.dto.GenericMessage<ChallengeDto> response = challengeService.startChallenge(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/options/{userId}")
    public ChallengeSelection getOptions(@PathVariable String userId) {
        return challengeService.getOptions(userId);
    }

    @PostMapping("/training-plan")
    public TrainingPlanResponse getTrainingPlan(@RequestBody Map<String, Integer> body) {
        Integer heartRate = body.get("heartRate");
        return challengeService.getTrainingPlan(heartRate);
    }
}
