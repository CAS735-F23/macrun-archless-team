/* (C)2023 */
package com.cas.playerservice.controller;

import com.cas.playerservice.dto.GenericMessage;
import com.cas.playerservice.dto.PlayerDto;
import com.cas.playerservice.dto.PlayerLoginRequest;
import com.cas.playerservice.dto.PlayerRegisterRequest;
import com.cas.playerservice.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "player")
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/register")
    public ResponseEntity<GenericMessage<PlayerDto>> register(
            @RequestBody PlayerRegisterRequest request) {
        GenericMessage<PlayerDto> response = playerService.register(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<GenericMessage<PlayerDto>> login(
            @RequestBody PlayerLoginRequest request) {
        GenericMessage<PlayerDto> response = playerService.login(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/game/start-game")
    public ResponseEntity<String> startGame() {
        playerService.startGame();
        return null;
    }
}
