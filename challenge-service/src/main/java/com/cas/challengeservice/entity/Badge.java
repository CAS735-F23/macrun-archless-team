package com.cas.challengeservice.entity;

import com.cas.challengeservice.dto.BadgeDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String challenge;
    private String username;
    private String badgeName;

    public BadgeDto toDto() {
        return BadgeDto.builder()
                .id(this.id)
                .challenge(this.challenge)
                .username(this.username)
                .badgeName(this.badgeName)
                .build();
    }
}
