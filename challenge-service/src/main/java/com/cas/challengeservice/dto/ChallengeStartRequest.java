package com.cas.challengeservice.dto;

public class ChallengeStartRequest {
    private String userId;

    public ChallengeStartRequest() {
    }

    public ChallengeStartRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}