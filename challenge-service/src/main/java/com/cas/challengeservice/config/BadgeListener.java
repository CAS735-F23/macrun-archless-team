/* (C)2023 */
package com.cas.challengeservice.config;

import static com.cas.challengeservice.constant.Constants.MQ_REQUEST_ADD_BADGE;

import com.cas.challengeservice.constant.Constants.*;
import com.cas.challengeservice.dto.BadgeAddRequest;
import com.cas.challengeservice.dto.MessageDto;
import com.cas.challengeservice.service.BadgeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class BadgeListener implements MessageListener {
    private BadgeService badgeService;

    private ObjectMapper objectMapper;

    @Autowired
    public BadgeListener(BadgeService badgeService) {
        this.badgeService = badgeService;
        objectMapper = new ObjectMapper();
    }

    @Override
    public void onMessage(Message message) {
        try {
            String messageBody = new String(message.getBody());
            log.info("Received message: " + messageBody);

            MessageDto messageDto = objectMapper.readValue(messageBody, MessageDto.class);

            switch (messageDto.getAction()) {
                case MQ_REQUEST_ADD_BADGE:
                    log.info("MQ - Adding badge: {}", messageDto.getBadgeAddRequest());
                    BadgeAddRequest badgeAddRequest = messageDto.getBadgeAddRequest();
                    badgeService.addBadge(badgeAddRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
