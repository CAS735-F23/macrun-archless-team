/* (C)2023 */
package com.cas.challengeservice.service.Impl;

import com.cas.challengeservice.service.MessageService;
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
}
