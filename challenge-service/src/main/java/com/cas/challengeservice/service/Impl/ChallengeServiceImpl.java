/* (C)2023 */
package com.cas.challengeservice.service.Impl;

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
            MessageService messageService,
            HttpSession httpSession) {
        this.challengeRepository = challengeRepository;
        this.messageService = messageService;
    }
    /**
     * get challenge
     *
     * @param request getChallenge request playload
     * @return 200 if getChallenge successful, 401 if heart rate is not in range, 500 if challenge not found
     */
    @Override
    public GenericMessage<ChallengeDto> getChallenge(ChallengeGetRequest request) {
        Challenge challenge = challengeRepository.findByUserHeartRate(request.getUserHeartRate()).orElse(null);
        GenericMessage<ChallengeDto> response;

        if (Objects.isNull(challenge)) {
            response =
                    GenericMessage.<ChallengeDto>builder()
                            .status(HttpStatus.NOT_ACCEPTABLE)
                            .message("No challenge assoicated with this heart rate")
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
}