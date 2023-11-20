package com.cas.challengeservice.service.Impl;

import com.cas.challengeservice.dto.*;
import com.cas.challengeservice.entity.Challenge;
import com.cas.challengeservice.repository.ChallengeRepository;
import com.cas.challengeservice.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class ChallengeServiceImpl implements ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final Map<Integer, String> trainingPlanMap;

    @Autowired
    public ChallengeServiceImpl(ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
        this.trainingPlanMap = new HashMap<>();
        initializeTrainingPlanMap();
    }

    private void initializeTrainingPlanMap() {
        trainingPlanMap.put(160, "做30个俯卧撑");
        // add more training plans as needed
    }

    @Override
    public GenericMessage<ChallengeDto> startChallenge(ChallengeStartRequest request) {
        Challenge challenge = challengeRepository.findById(request.getUserId())
                .orElseGet(() -> new Challenge(request.getUserId()));

        // You need to define the logic of starting a challenge based on your business requirements
        // For example, you might want to update the badge and score of the challenge

        challengeRepository.save(challenge);

        ChallengeDto challengeDto = new ChallengeDto(challenge.getId(), challenge.getBadge(), challenge.getScore());
        return new GenericMessage<>(HttpStatus.OK, "Challenge started successfully", challengeDto);
    }

    @Override
    public ChallengeSelection getOptions(String userId) {
        // Here I'm just returning hardcoded options, but you might want to determine these based on user's data
        return new ChallengeSelection("Option 1", "Option 2", "Option 3");
    }

    @Override
    public TrainingPlanResponse getTrainingPlan(int heartRate) {
        String trainingPlan = trainingPlanMap.get(heartRate);
        if (trainingPlan == null) {
            return new TrainingPlanResponse("No training plan available for the given heart rate");
        } else {
            return new TrainingPlanResponse(trainingPlan);
        }
    }
}