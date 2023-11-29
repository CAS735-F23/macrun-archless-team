/* (C)2023 */
package com.cas.challengeservice.service.Impl;

import com.cas.challengeservice.entity.ChallengeType;
import com.cas.challengeservice.dto.*;
import com.cas.challengeservice.repository.ChallengeTypeRepository;
import com.cas.challengeservice.service.ChallengeService;
import com.cas.challengeservice.service.MessageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Log4j2
public class ChallengeServiceImpl implements ChallengeService {
    private final ChallengeTypeRepository challengeTypeRepository;

    private List<ChallengeTypeDto> challengeTypes = new ArrayList<>(Arrays.asList(
            new ChallengeTypeDto(1L, "Cardio", 120L, 30L),
            new ChallengeTypeDto(2L, "Muscle", 80L, 50L),
            new ChallengeTypeDto(3L, "Flexibility", 70L, 40L),
            new ChallengeTypeDto(4L, "Balance", 70L, 30L)
    ));
    @Autowired
    public ChallengeServiceImpl(ChallengeTypeRepository challengeTypeRepository, MessageService messageService) {
        this.challengeTypeRepository = challengeTypeRepository;
    }
    /**
     * get challenge
     *
     * @param request getChallenge request playload
     * @return 200 if getChallenge successful, 401 if Heart rate is not in the range of this challenge typed, 404 if No challenge type associated with this type
     */
    @Override
    public GenericMessage<ChallengeTypeDto> getChallenge(ChallengeGetRequest request) {
        ChallengeTypeDto matchingChallengeType = null;

        // Try to find a challenge type that matches the request
        for (ChallengeTypeDto challengeType : challengeTypes) {
            if (challengeType.getDescription().equals(request.getType())) {
                if (request.getUserHeartRate() >= challengeType.getUserHeartRate() - 30 &&
                        request.getUserHeartRate() <= challengeType.getUserHeartRate() + 30) {
                    matchingChallengeType = challengeType;
                    break;
                } else {
                    return GenericMessage.<ChallengeTypeDto>builder()
                            .status(HttpStatus.UNAUTHORIZED)
                            .message("Heart rate is not in the range of this challenge type")
                            .build();
                }
            }
        }

        if (matchingChallengeType == null) {
            return GenericMessage.<ChallengeTypeDto>builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("No challenge type associated with this type")
                    .build();
        } else {
            return GenericMessage.<ChallengeTypeDto>builder()
                    .status(HttpStatus.OK)
                    .message("Challenge type found successfully, and returned")
                    .data(matchingChallengeType)
                    .build();
        }
    }

    /**
     * create challenge
     *
     * @param request createChallenge request playload
     * @return 200 if createChallenge successful, 409 if challenge already exists
     */
    @Override
    public GenericMessage<ChallengeTypeDto> addChallenge(ChallengeAddRequest request) {
        boolean exists = challengeTypes.stream()
                .anyMatch(challengeType -> challengeType.getDescription().equals(request.getType()));

        if (exists) {
            return GenericMessage.<ChallengeTypeDto>builder()
                    .status(HttpStatus.CONFLICT)
                    .message("Challenge type already exists.")
                    .build();
        }

        ChallengeTypeDto newChallengeType = new ChallengeTypeDto(
                new Random().nextLong(),
                request.getType(),
                request.getUserHeartRate(),
                request.getExerciseCount()
        );
        challengeTypes.add(newChallengeType);

        return GenericMessage.<ChallengeTypeDto>builder()
                .status(HttpStatus.CREATED)
                .message("Challenge type added successfully")
                .data(newChallengeType)
                .build();
    }
    /**
     * delete challenge
     *
     * @param request deleteChallenge request playload
     * @return 200 if deleteChallenge successful, 404 if challenge not found
     */
    @Override
    public GenericMessage<ChallengeTypeDto> deleteChallenge(ChallengeDeleteRequest request) {
        ChallengeTypeDto challengeTypeDto = challengeTypes.stream()
                .filter(challengeType -> challengeType.getDescription().equals(request.getType()))
                .findFirst()
                .orElse(null);

        if (challengeTypeDto == null) {
            return GenericMessage.<ChallengeTypeDto>builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Challenge type not found")
                    .build();
        }

        challengeTypes.remove(challengeTypeDto);

        return GenericMessage.<ChallengeTypeDto>builder()
                .status(HttpStatus.OK)
                .message("Challenge type deleted successfully")
                .data(challengeTypeDto)
                .build();
    }
}