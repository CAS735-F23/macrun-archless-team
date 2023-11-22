/* (C)2023 */
package com.cas.playerservice.controller;

import com.cas.playerservice.dto.*;
import com.cas.playerservice.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<GenericMessage<PlayerDto>> login(@RequestBody PlayerRequest request) {
        GenericMessage<PlayerDto> response = playerService.login(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<GenericMessage<Object>> logout(@RequestBody PlayerRequest request) {
        GenericMessage<Object> response = playerService.logout(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{username}/is-logged-in")
    public Boolean isPlayerLoggedIn(@PathVariable("username") String username) {
        return playerService.isPlayerLoggedIn(username);
    }

    @PostMapping("/set-location")
    public ResponseEntity<GenericMessage<Object>> setLocation(
            @RequestBody PlayerSetLocationRequest request) {
        GenericMessage<Object> response = playerService.setLocation(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
