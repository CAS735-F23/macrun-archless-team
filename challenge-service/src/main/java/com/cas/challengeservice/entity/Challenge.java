package com.cas.challengeservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private int badge;
    private int score;

    public Challenge(String userId) {
    }

    public Challenge() {

    }

    // getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
