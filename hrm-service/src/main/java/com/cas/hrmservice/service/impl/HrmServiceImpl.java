/* (C)2023 */
package com.cas.hrmservice.service.impl;

import static com.cas.hrmservice.constant.Constants.HRM_ACTION_SEND_HEART_RATE;
import static com.cas.hrmservice.constant.Constants.MQ_HRM_TRANSMIT;

import com.cas.hrmservice.dto.GenericMessage;
import com.cas.hrmservice.dto.HeartRateDto;
import com.cas.hrmservice.dto.HrmReceiveRequest;
import com.cas.hrmservice.dto.MessageDto;
import com.cas.hrmservice.scheduler.SendingHeartRateScheduler;
import com.cas.hrmservice.service.HrmService;
import com.cas.hrmservice.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class HrmServiceImpl implements HrmService {
    @Value("${spring.rabbitmq.game.exchange}")
    private String gameExchangeName;

    private final MessageService messageService;

    private final SendingHeartRateScheduler sendingHeartRateScheduler;

    private final ObjectMapper objectMapper;

    @Autowired
    public HrmServiceImpl(
            MessageService messageService,
            @Lazy SendingHeartRateScheduler sendingHeartRateScheduler) {
        this.messageService = messageService;
        this.sendingHeartRateScheduler = sendingHeartRateScheduler;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void startHrm(String username) {
        sendingHeartRateScheduler.start(username);
    }

    @Override
    public void stopHrm(String username) {
        sendingHeartRateScheduler.stop(username);
    }

    @Override
    public GenericMessage<HeartRateDto> transmitHeartRate(HrmReceiveRequest request) {
        if (0 > request.getHeartRate() || request.getHeartRate() > 400) {
            return GenericMessage.<HeartRateDto>builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .data(request.toDto())
                    .message("heart rate is not valid, please double check...")
                    .build();
        } else {
            boolean response = sendHeartRate(request.getUsername(), request.getHeartRate());
            if (response) {
                log.info("SCHEDULER - hr to game service sent...");

                return GenericMessage.<HeartRateDto>builder()
                        .status(HttpStatus.OK)
                        .data(request.toDto())
                        .message("successfully sent heart rate to game service...")
                        .build();
            } else {
                log.info("SCHEDULER - hr to game service failed to send...");

                return GenericMessage.<HeartRateDto>builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .data(request.toDto())
                        .message("failed to sent heart rate to game service...")
                        .build();
            }
        }
    }

    private Boolean sendHeartRate(String username, Integer heartRate) {
        HeartRateDto heartRateDto =
                HeartRateDto.builder().username(username).heartRate(heartRate).build();
        MessageDto messageDto =
                MessageDto.builder()
                        .action(HRM_ACTION_SEND_HEART_RATE)
                        .heartRateDto(heartRateDto)
                        .build();

        try {
            String heartRateMsg = objectMapper.writeValueAsString(messageDto);
            messageService.sendMessage(gameExchangeName, MQ_HRM_TRANSMIT, heartRateMsg);
            return true;
        } catch (JsonProcessingException e) {
            log.error("failed to serialize object: " + messageDto.toString() + ", reason: " + e);
            return false;
        }
    }
}
