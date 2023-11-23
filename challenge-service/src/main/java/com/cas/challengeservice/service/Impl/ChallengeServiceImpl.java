/* (C)2023 */
package com.cas.challengeservice.service.Impl;

import com.cas.challengeservice.constants.ChallengeType;
import com.cas.challengeservice.dto.*;
import com.cas.challengeservice.entity.Challenge;
import com.cas.challengeservice.repository.ChallengeRepository;
import com.cas.challengeservice.service.ChallengeService;
import com.cas.challengeservice.service.MessageService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Log4j2
public class ChallengeServiceImpl implements ChallengeService {
    @Value("${spring.rabbitmq.queue}")
    private String queueName;

    private final ChallengeRepository challengeRepository;

    private final MessageService messageService;

    @Autowired
    public ChallengeServiceImpl(
            ChallengeRepository challengeRepository,
            MessageService messageService) {
        this.challengeRepository = challengeRepository;
        this.messageService = messageService;
    }
    /**
     * get challenge
     *
     * @param request getChallenge request playload
     * @return 200 if getChallenge successful, 401 if heart rate is not in range or type invalid, 500 if challenge not found
     */
    @Override
    public GenericMessage<ChallengeDto> getChallenge(ChallengeGetRequest request) {
        Challenge challenge = challengeRepository.findByUserHeartRateAndType(request.getUserHeartRate(), request.getType()).orElse(null);
        GenericMessage<ChallengeDto> response;

        if (Objects.isNull(challenge)) {
            response =
                    GenericMessage.<ChallengeDto>builder()
                            .status(HttpStatus.NOT_ACCEPTABLE)
                            .message("No challenge associated with this heart rate and type")
                            .build();
        } else {
            response =
                    GenericMessage.<ChallengeDto>builder()
                            .status(HttpStatus.OK)
                            .message("Challenge found successfully, and returned")
                            .data(challenge.toDto())
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
    public GenericMessage<ChallengeDto> addChallenge(ChallengeAddRequest request) {
        ChallengeType challengeType = Arrays.stream(ChallengeType.values())
                .filter(type -> type.getDescription().equals(request.getType())
                        && type.getAverageHeartRate() == request.getUserHeartRate())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("ChallengeType not found"));

        if (challengeRepository.existsByChallengeType(challengeType)) {
            return GenericMessage.<ChallengeDto>builder()
                    .status(HttpStatus.CONFLICT)
                    .message("A challenge with this type already exists")
                    .build();
        }

        Challenge challenge = Challenge.builder()
                .challengeType(challengeType)
                .build();

        // 保存 Challenge
        challengeRepository.save(challenge);

        // 创建响应
        GenericMessage<ChallengeDto> response = GenericMessage.<ChallengeDto>builder()
                .status(HttpStatus.CREATED)
                .message("Challenge added successfully")
                .data(challenge.toDto())
                .build();

        return response;
    }

    @Override
    public GenericMessage<Void> deleteChallenge(ChallengeDeleteRequest request) {
        // 根据 description 和 averageHeartRate 找到对应的 ChallengeType
        ChallengeType challengeType = Arrays.stream(ChallengeType.values())
                .filter(type -> type.getDescription().equals(request.getType())
                        && type.getAverageHeartRate() == request.getUserHeartRate())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("ChallengeType not found"));

        // 查找并删除 Challenge
        challengeRepository.deleteByChallengeType(challengeType);

        // 创建响应
        GenericMessage<Void> response = GenericMessage.<Void>builder()
                .status(HttpStatus.OK)
                .message("Challenge deleted successfully")
                .build();

        return response;
    }
}