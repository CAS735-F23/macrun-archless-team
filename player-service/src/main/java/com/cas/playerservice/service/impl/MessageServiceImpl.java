/* (C)2023 */
package com.cas.playerservice.service.impl;

import com.cas.playerservice.service.MessageService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageServiceImpl implements MessageService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendMessage(String exchangeName, String route, String message) {
        rabbitTemplate.convertAndSend(exchangeName, route, message);
    }
}
