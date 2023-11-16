package com.cas.playerservice.service;

public interface MessageService {
    void sendMessage(String exchangeName, String route, String message);
}
