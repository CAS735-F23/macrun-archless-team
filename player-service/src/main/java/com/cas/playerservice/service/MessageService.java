/* (C)2023 */
package com.cas.playerservice.service;

import org.springframework.stereotype.Service;

@Service
public interface MessageService {
    void sendMessage(String exchangeName, String route, String message);
}
