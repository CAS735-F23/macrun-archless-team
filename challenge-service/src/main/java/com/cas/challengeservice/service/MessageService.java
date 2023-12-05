package com.cas.challengeservice.service;

public interface MessageService {
    void sendMessage(String exchangeName, String route, String message);
}
