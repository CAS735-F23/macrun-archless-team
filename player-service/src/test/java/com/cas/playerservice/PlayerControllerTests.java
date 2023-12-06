/* (C)2023 */
package com.cas.playerservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.cas.playerservice.controller.PlayerController;
import com.cas.playerservice.dto.*;
import com.cas.playerservice.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class PlayerControllerTests {
    @Mock private PlayerService playerService;

    @InjectMocks private PlayerController playerController;

    private String username;

    private PlayerRegisterRequest playerRegisterRequest;

    private PlayerRequest playerRequest;

    private GenericMessage<PlayerDto> message;

    @BeforeEach
    public void setUp() {
        username = "username";
        playerRegisterRequest =
                PlayerRegisterRequest.builder().username(username).password("password").build();
        playerRequest = PlayerRequest.builder().username(username).password("password").build();

        message =
                GenericMessage.<PlayerDto>builder()
                        .status(HttpStatus.OK)
                        .message("player response")
                        .build();
    }

    @Test
    public void testPlayerRegister() {
        when(playerService.register(any())).thenReturn(message);

        ResponseEntity<GenericMessage<PlayerDto>> responseEntity =
                playerController.register(playerRegisterRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(playerService, times(1)).register(any());
    }

    @Test
    public void testPlayerLogin() {
        when(playerService.login(any())).thenReturn(message);

        ResponseEntity<GenericMessage<PlayerDto>> responseEntity =
                playerController.login(playerRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(playerService, times(1)).login(any());
    }

    @Test
    public void testPlayerLogout() {
        GenericMessage<Object> logoutMsg =
                GenericMessage.builder().status(HttpStatus.OK).message("logout").build();
        when(playerService.logout(any())).thenReturn(logoutMsg);

        ResponseEntity<GenericMessage<Object>> responseEntity =
                playerController.logout(playerRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(playerService, times(1)).logout(any());
    }

    @Test
    public void testIsLoggedIn() {
        when(playerService.isPlayerLoggedIn(any())).thenReturn(true);

        boolean isLoggedIn = playerController.isPlayerLoggedIn(username);

        assertTrue(isLoggedIn);
        verify(playerService, times(1)).isPlayerLoggedIn(any());
    }

    @Test
    public void testSetLocation() {
        GenericMessage<Object> logoutMsg =
                GenericMessage.builder().status(HttpStatus.OK).message("logout").build();
        when(playerService.setZone(any())).thenReturn(logoutMsg);

        PlayerSetZoneRequest request =
                PlayerSetZoneRequest.builder().username(username).zone("TEST").build();

        ResponseEntity<GenericMessage<Object>> responseEntity =
                playerController.setLocation(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(playerService, times(1)).setZone(any());
    }
}
