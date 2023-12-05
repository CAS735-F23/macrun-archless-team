package com.cas.challengeservice.dto;

import lombok.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class BadgeDto {
    private Long id;
    private String challenge;
    private String username;
    private String badgeName;
}
