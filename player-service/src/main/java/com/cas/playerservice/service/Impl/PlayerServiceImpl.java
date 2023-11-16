package com.cas.playerservice.service.Impl;

import com.cas.playerservice.dto.*;
import com.cas.playerservice.entity.Player;
import com.cas.playerservice.repository.PlayerRepository;
import com.cas.playerservice.service.MessageService;
import com.cas.playerservice.service.PlayerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import java.util.Objects;

import static com.cas.playerservice.constant.Constants.*;

@Component
@Log4j2
public class PlayerServiceImpl implements PlayerService {
    @Value("${spring.rabbitmq.queue}")
    private String queueName;

    private HttpSession httpSession;

    private final PlayerRepository playerRepository;

    private final MessageService messageService;

    private final ObjectMapper objectMapper;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, MessageService messageService, HttpSession httpSession) {
        this.messageService = messageService;
        this.playerRepository = playerRepository;
        this.httpSession = httpSession;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * player register
     * @param request register request payload
     * @return 200 if register successful, else return 500
     */
    @Override
    public GenericMessage<PlayerDto> register(PlayerRegisterRequest request) {
        Player player = Player.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .weight(request.getWeight())
                .age(request.getAge())
                .build();

        if(Objects.nonNull(playerRepository.findByUsername(request.getUsername()).orElse(null))) {
            log.info("Player already exists for : [{}]", player.getUsername());
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
            log.info("Player Registration failed for : [{}], reason : [{}]", player.getUsername(), e);
            return GenericMessage.<PlayerDto>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Player Registration failed...")
                    .data(player.toDto())
                    .build();
        }
    }

    /**
     * player login
     * @param request login request playload
     * @return 200 if login successful, 401 if password is wrong, 500 if player not found
     */
    @Override
    public GenericMessage<PlayerDto> login(PlayerLoginRequest request) {
        Player player = playerRepository.findByUsername(request.getUsername()).orElse(null);
        GenericMessage<PlayerDto> response;

        if(Objects.isNull(player)) {
            response = GenericMessage.<PlayerDto>builder()
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .message("No player associated with this username, please check...")
                    .build();
        } else {
            if(Objects.equals(player.getPassword(), request.getPassword())) {
                String playerJson;
                try {
                    playerJson = objectMapper.writeValueAsString(player.toDto());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                httpSession.setAttribute(CACHE_PLAYER_SESSION, playerJson);
                response = GenericMessage.<PlayerDto>builder()
                        .status(HttpStatus.OK)
                        .message("Login successful...")
                        .data(player.toDto())
                        .build();
            } else {
                response = GenericMessage.<PlayerDto>builder()
                        .status(HttpStatus.UNAUTHORIZED)
                        .message("Login failed, password is wrong...")
                        .build();
            }
        }

        return response;

    }

    @Override
    public void startGame() {
        String playerObj = httpSession.getAttribute(CACHE_PLAYER_SESSION).toString();
        System.out.println(httpSession.getAttribute(CACHE_PLAYER_SESSION));

        PlayerDto playerDto;
        try {
            playerDto = objectMapper.readValue(playerObj, PlayerDto.class);
            MessageDto messageDto = MessageDto.builder()
                    .gameType(GAME_TYPE_CARDIO)
                    .action(PLAYER_ACTION_START_GAME)
                    .playerDto(playerDto)
                    .build();

            messageService.sendMessage(MQ_GAME_SERVICE_EXCHANGE, queueName, objectMapper.writeValueAsString(messageDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }


}
