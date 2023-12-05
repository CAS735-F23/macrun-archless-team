/* (C)2023 */
package com.cas.challengeservice.service.Impl;

import com.cas.challengeservice.dto.*;
import com.cas.challengeservice.entity.ChallengeType;
import com.cas.challengeservice.repository.ChallengeTypeRepository;
import com.cas.challengeservice.service.ChallengeService;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class ChallengeServiceImpl implements ChallengeService {
    private final ChallengeTypeRepository challengeTypeRepository;

    //    private List<ChallengeTypeDto> challengeTypes = new ArrayList<>(Arrays.asList(
    //            new ChallengeTypeDto(1L, "Cardio", 120L, 30L),
    //            new ChallengeTypeDto(2L, "Muscle", 80L, 50L),
    //            new ChallengeTypeDto(3L, "Flexibility", 70L, 40L),
    //            new ChallengeTypeDto(4L, "Balance", 70L, 30L)
    //    ));
    @Autowired
    public ChallengeServiceImpl(ChallengeTypeRepository challengeTypeRepository) {
        this.challengeTypeRepository = challengeTypeRepository;
    }

    /**
     * get challenge
     *
     * @param request getChallenge request playload
     * @return 200 if getChallenge successful, 401 if Heart rate is not in the range of this
     *     challenge typed, 404 if No challenge type associated with this type
     */
    @Override
    public GenericMessage<ChallengeTypeDto> getChallenge(ChallengeGetRequest request) {
        Optional<ChallengeType> matchingChallengeType =
                challengeTypeRepository.findByDescription(request.getType());

        if (matchingChallengeType.isPresent()) {
            ChallengeType challengeType = matchingChallengeType.get();
            if (request.getUserHeartRate() >= challengeType.getUserHeartRate() - 30
                    && request.getUserHeartRate() <= challengeType.getUserHeartRate() + 30) {
                return GenericMessage.<ChallengeTypeDto>builder()
                        .status(HttpStatus.OK)
                        .message("Challenge type found successfully, and returned")
                        .data(challengeType.toDto())
                        .build();
            } else {
                return GenericMessage.<ChallengeTypeDto>builder()
                        .status(HttpStatus.UNAUTHORIZED)
                        .message("Heart rate is not in the range of this challenge type")
                        .build();
            }
        } else {
            return GenericMessage.<ChallengeTypeDto>builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("No challenge type associated with this type")
                    .build();
        }
    }

    /**
     * add challenge
     *
     * @param request createChallenge request playload
     * @return 200 if createChallenge successful, 409 if challenge already exists
     */
    @Override
    public GenericMessage<ChallengeTypeDto> addChallenge(ChallengeAddRequest request) {
        Optional<ChallengeType> existingChallengeType =
                challengeTypeRepository.findByDescription(request.getType());

        if (existingChallengeType.isPresent()) {
            return GenericMessage.<ChallengeTypeDto>builder()
                    .status(HttpStatus.CONFLICT)
                    .message("Challenge type already exists.")
                    .build();
        }

        ChallengeType newChallengeType =
                new ChallengeType(
                        new Random().nextLong(),
                        request.getType(),
                        request.getUserHeartRate(),
                        request.getExerciseCount());
        challengeTypeRepository.save(newChallengeType);

        return GenericMessage.<ChallengeTypeDto>builder()
                .status(HttpStatus.CREATED)
                .message("Challenge type added successfully")
                .data(newChallengeType.toDto())
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
        Optional<ChallengeType> existingChallengeType =
                challengeTypeRepository.findByDescription(request.getType());

        if (!existingChallengeType.isPresent()) {
            return GenericMessage.<ChallengeTypeDto>builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Challenge type not found")
                    .build();
        }

        challengeTypeRepository.delete(existingChallengeType.get());

        return GenericMessage.<ChallengeTypeDto>builder()
                .status(HttpStatus.OK)
                .message("Challenge type deleted successfully")
                .data(existingChallengeType.get().toDto())
                .build();
    }
}
