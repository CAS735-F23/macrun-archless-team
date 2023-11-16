package com.cas.playerservice.service.Impl;

import com.cas.playerservice.service.MessageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {
    @Value("${spring.rabbitmq.queue}")
    private String queueName;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendMessage(String exchangeName, String route, String message) {
        rabbitTemplate.convertAndSend(exchangeName, route, message);
    }

//    @RabbitListener(queues = "player")
//    public void receiveMessage(String message) {
//        System.out.println("Received message: " + message);
//    }
}

