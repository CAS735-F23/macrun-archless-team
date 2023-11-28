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
     * @return 200 if getChallenge successful, 401 if heart rate is not in range or type invalid, 500 if challenge not found
     */
    @Override
    public GenericMessage<ChallengeTypeDto> getChallenge(ChallengeGetRequest request) {
//        Optional<ChallengeType> challengeTypeOptional = challengeTypeRepository.findByDescriptionAndUserHeartRate(request.getType(), request.getUserHeartRate());
//        GenericMessage<ChallengeTypeDto> response;
//
//        if (challengeTypeOptional.isEmpty()) {
//            response = GenericMessage.<ChallengeTypeDto>builder()
//                    .status(HttpStatus.NOT_FOUND)
//                    .message("No challenge type associated with this heart rate and type")
//                    .build();
//        } else {
//            ChallengeType challengeType = challengeTypeOptional.get();
//            response = GenericMessage.<ChallengeTypeDto>builder()
//                    .status(HttpStatus.OK)
//                    .message("Challenge type found successfully, and returned")
//                    .data(challengeType.toDto())
//                    .build();
//        }
//
//        return response;
        ChallengeTypeDto matchingChallengeType = null;

        // Try to find a challenge type that matches the request
        for (ChallengeTypeDto challengeType : challengeTypes) {
            System.out.println("=============================Challenge type:=============================");
            System.out.println(challengeType);
            System.out.println(request);
            if (challengeType.getDescription().equals(request.getType()) &&
                    challengeType.getUserHeartRate().equals(request.getUserHeartRate())) {
                matchingChallengeType = challengeType;
                break;
            }
        }

        GenericMessage<ChallengeTypeDto> response;

        if (matchingChallengeType == null) {
            response = GenericMessage.<ChallengeTypeDto>builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("No challenge type associated with this heart rate and type")
                    .build();
        } else {
            response = GenericMessage.<ChallengeTypeDto>builder()
                    .status(HttpStatus.OK)
                    .message("Challenge type found successfully, and returned")
                    .data(matchingChallengeType)
                    .build();
        }

        return response;
    }

    /**
     * create challenge
     *
     * @param request createChallenge request playload
     * @return 200 if createChallenge successful, 409 if challenge already exists
     */
    @Override
    public GenericMessage<ChallengeTypeDto> addChallenge(ChallengeAddRequest request) {
//        Optional<ChallengeType> existingChallengeType = challengeTypeRepository.findByDescriptionAndUserHeartRateAndExerciseCount(request.getType(), request.getUserHeartRate(), request.getExerciseCount());
//
//        if (existingChallengeType.isPresent()) {
//            return GenericMessage.<ChallengeTypeDto>builder()
//                    .status(HttpStatus.CONFLICT)
//                    .message("A challenge type with this type and user heart rate already exists")
//                    .build();
//        }
//
//        ChallengeType newChallengeType = new ChallengeType(null, request.getType(), request.getUserHeartRate(), request.getExerciseCount());
//        challengeTypeRepository.save(newChallengeType);
//
//        GenericMessage<ChallengeTypeDto> response = GenericMessage.<ChallengeTypeDto>builder()
//                .status(HttpStatus.CREATED)
//                .message("Challenge type added successfully")
//                .data(newChallengeType.toDto())
//                .build();
//
//        return response;

        boolean exists = challengeTypes.stream()
                .anyMatch(challengeType ->
                        challengeType.getDescription().equals(request.getType()) &&
                                challengeType.getUserHeartRate().equals(request.getUserHeartRate()) &&
                                challengeType.getExerciseCount().equals(request.getExerciseCount()));

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

    @Override
    public GenericMessage<ChallengeTypeDto> deleteChallenge(ChallengeDeleteRequest request) {
//        Optional<ChallengeType> challengeType = challengeTypeRepository.findByDescriptionAndUserHeartRateAndExerciseCount(request.getType(), request.getUserHeartRate(), request.getExerciseCount());
//
//        if (challengeType.isEmpty()) {
//            return GenericMessage.<Void>builder()
//                    .status(HttpStatus.NOT_FOUND)
//                    .message("Challenge type not found")
//                    .build();
//        }
//
//        challengeTypeRepository.delete(challengeType.get());
//
//        GenericMessage<Void> response = GenericMessage.<Void>builder()
//                .status(HttpStatus.OK)
//                .message("Challenge type deleted successfully")
//                .build();
//
//        return response;
        ChallengeTypeDto challengeTypeDto = challengeTypes.stream()
                .filter(challengeType ->
                        challengeType.getDescription().equals(request.getType()) &&
                                challengeType.getUserHeartRate().equals(request.getUserHeartRate()) &&
                                challengeType.getExerciseCount().equals(request.getExerciseCount()))
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