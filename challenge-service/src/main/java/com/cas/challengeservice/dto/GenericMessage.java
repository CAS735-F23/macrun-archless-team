package com.cas.challengeservice.dto;

import org.springframework.http.HttpStatus;

public class GenericMessage<T> {
    private HttpStatus status;
    private String message;
    private T data;

    public GenericMessage(HttpStatus httpStatus, String challengeStartedSuccessfully, T challengeDto) {
    }

    public int getStatus() {
        return status.value();
    }
    // getters and setters
}