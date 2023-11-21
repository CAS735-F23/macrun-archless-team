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

    @PostMapping("/get-challenge")
    public ResponseEntity<GenericMessage<ChallengeDto>> getChallenge(
            @RequestBody ChallengeGetRequest request) {
        GenericMessage<ChallengeDto> response = challengeService.getChallenge(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
