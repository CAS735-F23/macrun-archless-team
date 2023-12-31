/* (C)2023 */
package com.cas.challengeservice.controller;

import com.cas.challengeservice.dto.*;
import com.cas.challengeservice.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/challenge")
public class ChallengeController {

  private final ChallengeService challengeService;

  @Autowired
  public ChallengeController(ChallengeService challengeService) {
    this.challengeService = challengeService;
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<GenericMessage<ChallengeTypeDto>> getChallenge(
      @RequestParam Long userHeartRate, @RequestParam String type) {
    ChallengeGetRequest request = new ChallengeGetRequest(userHeartRate, type);
    GenericMessage<ChallengeTypeDto> response = challengeService.getChallenge(request);
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<GenericMessage<ChallengeTypeDto>> addChallenge(
      @RequestBody ChallengeAddRequest request) {
    GenericMessage<ChallengeTypeDto> response = challengeService.addChallenge(request);
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @RequestMapping(method = RequestMethod.DELETE)
  public ResponseEntity<GenericMessage<ChallengeTypeDto>> deleteChallenge(
      @RequestBody ChallengeDeleteRequest request) {
    GenericMessage<ChallengeTypeDto> response = challengeService.deleteChallenge(request);
    return ResponseEntity.status(response.getStatus()).body(response);
  }
}
