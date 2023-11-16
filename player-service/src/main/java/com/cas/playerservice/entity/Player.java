package com.cas.playerservice.entity;

import com.cas.playerservice.dto.PlayerDto;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
@Table
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private Integer weight;
    private Integer age;

    public PlayerDto toDto() {
        return PlayerDto.builder()
                .username(username)
                .email(email)
                .weight(weight)
                .age(age)
                .build();
    }
}
