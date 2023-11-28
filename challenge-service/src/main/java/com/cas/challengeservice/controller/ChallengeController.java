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
    public ResponseEntity<GenericMessage<ChallengeTypeDto>> getChallenge(
            @RequestBody ChallengeGetRequest request) {
        GenericMessage<ChallengeTypeDto> response = challengeService.getChallenge(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/add-challenge")
    public ResponseEntity<GenericMessage<ChallengeTypeDto>> addChallenge(
            @RequestBody ChallengeAddRequest request) {
        GenericMessage<ChallengeTypeDto> response = challengeService.addChallenge(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/delete-challenge")
    public ResponseEntity<GenericMessage<Void>> deleteChallenge(
            @RequestBody ChallengeDeleteRequest request) {
        GenericMessage<Void> response = challengeService.deleteChallenge(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
