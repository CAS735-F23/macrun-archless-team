package com.cas.challengeservice.service;

import com.cas.challengeservice.dto.*;

public interface ChallengeService {
    GenericMessage<ChallengeDto> startChallenge(ChallengeStartRequest request);
    ChallengeSelection getOptions(String userId);
    TrainingPlanResponse getTrainingPlan(int heartRate);
}
