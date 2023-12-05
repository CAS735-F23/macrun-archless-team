/* (C)2023 */
package com.cas.playerservice.service.impl;

import static com.cas.playerservice.constant.Constants.*;

import com.cas.playerservice.dto.*;
import com.cas.playerservice.entity.Player;
import com.cas.playerservice.repository.PlayerRepository;
import com.cas.playerservice.service.MessageService;
import com.cas.playerservice.service.PlayerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Objects;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class PlayerServiceImpl implements PlayerService {
    @Value("${spring.rabbitmq.geo.queue}")
    private String queueName;

    private HttpSession httpSession;

    private final PlayerRepository playerRepository;

    private final MessageService messageService;

    private final ObjectMapper objectMapper;

    @Autowired
    public PlayerServiceImpl(
            PlayerRepository playerRepository,
            MessageService messageService,
            HttpSession httpSession) {
        this.messageService = messageService;
        this.playerRepository = playerRepository;
        this.httpSession = httpSession;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * player register
     *
     * @param request register request payload
     * @return 200 if register successful, else return 500
     */
    @Override
    public GenericMessage<PlayerDto> register(PlayerRegisterRequest request) {
        Player player =
                Player.builder()
                        .username(request.getUsername())
                        .password(request.getPassword())
                        .email(request.getEmail())
                        .weight(request.getWeight())
                        .age(request.getAge())
                        .build();

        if (Objects.nonNull(playerRepository.findByUsername(request.getUsername()).orElse(null))) {
            log.error("Player already exists for : [{}]", player.getUsername());
            return GenericMessage.<PlayerDto>builder()
                    .status(HttpStatus.FORBIDDEN)
                    .message("Player already exists...")
                    .data(player.toDto())
                    .build();
        }

        try {
            playerRepository.save(player);

            log.info("Player Registration successful for : [{}]", player.getUsername());
            return GenericMessage.<PlayerDto>builder()
                    .status(HttpStatus.OK)
                    .message("Player Registration successful...")
                    .data(player.toDto())
                    .build();

        } catch (RuntimeException e) {
            log.info(
                    "Player Registration failed for : [{}], reason : [{}]",
                    player.getUsername(),
                    e);
            return GenericMessage.<PlayerDto>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Player Registration failed...")
                    .data(player.toDto())
                    .build();
        }
    }

    /**
     * player login
     *
     * @param request login request payload
     * @return 200 if login successful, 401 if password is wrong, 500 if player not found
     */
    @Override
    public GenericMessage<PlayerDto> login(PlayerRequest request) {
        Player player = playerRepository.findByUsername(request.getUsername()).orElse(null);
        GenericMessage<PlayerDto> response;

        if (Objects.isNull(player)) {
            response =
                    GenericMessage.<PlayerDto>builder()
                            .status(HttpStatus.NOT_ACCEPTABLE)
                            .message("No player associated with this username, please check...")
                            .build();
        } else {
            if (Objects.equals(player.getPassword(), request.getPassword())) {
                String playerJson;
                try {
                    playerJson = objectMapper.writeValueAsString(player.toDto());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                httpSession.setAttribute(CACHE_PLAYER_SESSION + request.getUsername(), playerJson);
                response =
                        GenericMessage.<PlayerDto>builder()
                                .status(HttpStatus.OK)
                                .message("Login successful...")
                                .data(player.toDto())
                                .build();
            } else {
                response =
                        GenericMessage.<PlayerDto>builder()
                                .status(HttpStatus.UNAUTHORIZED)
                                .message("Login failed, password is wrong...")
                                .build();
            }
        }

        return response;
    }

    @Override
    public GenericMessage<Object> logout(PlayerRequest request) {
        try {
            if (!isPlayerLoggedIn(request.getUsername())) {
                log.error("Logout failed, Player is not logged in...");
                return GenericMessage.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("Logout failed, player is not logged in...")
                        .build();
            }

            httpSession.removeAttribute(CACHE_PLAYER_SESSION + request.getUsername());
            return GenericMessage.builder()
                    .status(HttpStatus.OK)
                    .message("Logout successful, Player has been logged out...")
                    .build();
        } catch (Exception e) {
            log.error("Logout failed, failed to logout player...");
            return GenericMessage.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Logout failed, please try again...")
                    .build();
        }
    }

    @Override
    public GenericMessage<Object> setLocation(PlayerSetLocationRequest request) {
        if (!isPlayerLoggedIn(request.getUsername())) {
            log.error("player is not logged in...");
            return GenericMessage.<Object>builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("SetLocation failed, player is not logged in....")
                    .build();
        }

        try {
            Location.valueOf(request.getLocation());
        } catch (IllegalArgumentException ex) {
            return GenericMessage.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message(
                            "Location you set is not in the available zone : "
                                    + Arrays.toString(Location.values()))
                    .build();
        }

        String playerObj =
                httpSession.getAttribute(CACHE_PLAYER_SESSION + request.getUsername()).toString();

        PlayerDto playerDto;
        try {
            playerDto = objectMapper.readValue(playerObj, PlayerDto.class);
            MessageDto messageDto =
                    MessageDto.builder()
                            .action(PLAYER_ACTION_SET_LOCATION)
                            .playerDto(playerDto)
                            .message(request.getLocation())
                            .build();

            messageService.sendMessage(
                    MQ_GAME_SERVICE_EXCHANGE,
                    queueName,
                    objectMapper.writeValueAsString(messageDto));

            return GenericMessage.builder()
                    .status(HttpStatus.OK)
                    .message(
                            "Successfully sent set location request as location: "
                                    + request.getLocation())
                    .build();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean isPlayerLoggedIn(String username) {
        log.info("check if user is logged in" + CACHE_PLAYER_SESSION + username);
        return Objects.nonNull(httpSession.getAttribute(CACHE_PLAYER_SESSION + username));
    }
}
