package com.cas.challengeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BadgeDeleteRequest {
    private String challenge;
    private String username;
    private String badgeName;
}
