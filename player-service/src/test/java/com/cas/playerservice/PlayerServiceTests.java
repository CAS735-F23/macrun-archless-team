/* (C)2023 */
package com.cas.playerservice;

import static com.cas.playerservice.constant.Constants.CACHE_PLAYER_SESSION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cas.playerservice.dto.*;
import com.cas.playerservice.entity.Player;
import com.cas.playerservice.repository.PlayerRepository;
import com.cas.playerservice.service.MessageService;
import com.cas.playerservice.service.impl.PlayerServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
public class PlayerServiceTests {
    @Mock private PlayerRepository playerRepository;

    @Mock private HttpSession httpSession;

    @Mock private MessageService messageService;

    private ObjectMapper objectMapper;

    @InjectMocks private PlayerServiceImpl playerServiceImpl;

    private PlayerRequest request;

    @BeforeEach
    public void setUp() {
        request = PlayerRequest.builder().username("username").password("password").build();
        objectMapper = new ObjectMapper();
    }

    /** Registration */
    @Test
    public void testRegisterSuccess() {
        PlayerRegisterRequest request =
                new PlayerRegisterRequest("username", "password", "email", 70, 25);
        when(playerRepository.findByUsername(any())).thenReturn(java.util.Optional.empty());

        GenericMessage<PlayerDto> result = playerServiceImpl.register(request);

        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("Player Registration successful...", result.getMessage());
    }

    @Test
    public void testRegisterFailedWithExistingUser() {
        PlayerRegisterRequest request =
                new PlayerRegisterRequest("username", "password", "email", 70, 25);
        Player player = Player.builder().username("username").build();
        when(playerRepository.findByUsername(any())).thenReturn(Optional.ofNullable(player));

        GenericMessage<PlayerDto> result = playerServiceImpl.register(request);

        assertEquals(HttpStatus.FORBIDDEN, result.getStatus());
        assertEquals("Player already exists...", result.getMessage());
    }

    @Test
    public void testRegisterFailedWithException() {
        PlayerRegisterRequest request =
                new PlayerRegisterRequest("username", "password", "email", 70, 25);
        when(playerRepository.save(any())).thenThrow(new RuntimeException("server error"));

        GenericMessage<PlayerDto> result = playerServiceImpl.register(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatus());
        assertEquals("Player Registration failed...", result.getMessage());
    }

    @Test
    public void testLoginSuccess() {
        Player player = Player.builder().username("username").password("password").build();
        when(playerRepository.findByUsername(any())).thenReturn(Optional.ofNullable(player));

        GenericMessage<PlayerDto> result = playerServiceImpl.login(request);

        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("Login successful...", result.getMessage());
    }

    @Test
    public void testLoginFailedWithNonExistingUser() {
        when(playerRepository.findByUsername(any())).thenReturn(Optional.empty());

        GenericMessage<PlayerDto> result = playerServiceImpl.login(request);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, result.getStatus());
        assertEquals(
                "No player associated with this username, please check...", result.getMessage());
    }

    @Test
    public void testLoginWFailedithWrongPassword() {
        request.setPassword("wrong-password");
        Player player = Player.builder().username("username").password("password").build();
        when(playerRepository.findByUsername(any())).thenReturn(Optional.ofNullable(player));

        GenericMessage<PlayerDto> result = playerServiceImpl.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatus());
        assertEquals("Login failed, password is wrong...", result.getMessage());
    }

    @Test
    public void testIsPlayerLoggedInSuccess() {
        String usernameCachedKey = CACHE_PLAYER_SESSION + "username";
        when(httpSession.getAttribute(anyString())).thenReturn("user_httpSession");

        Boolean result = playerServiceImpl.isPlayerLoggedIn(usernameCachedKey);

        assertTrue(result);
    }

    @Test
    public void testIsPlayerLoggedInFailedWithNotLoggedIn() {
        String usernameCachedKey = CACHE_PLAYER_SESSION + "username";
        when(httpSession.getAttribute(anyString())).thenReturn(null);

        Boolean result = playerServiceImpl.isPlayerLoggedIn(usernameCachedKey);

        assertFalse(result);
    }

    @Test
    public void testLogoutSuccess() {
        when(httpSession.getAttribute(anyString())).thenReturn("user_httpSession");
        when(playerServiceImpl.isPlayerLoggedIn(any())).thenReturn(true);

        GenericMessage<Object> result = playerServiceImpl.logout(request);

        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("Logout successful, Player has been logged out...", result.getMessage());
    }

    @Test
    public void testLogoutSuccessWithCacheSessionRemoved() {
        when(httpSession.getAttribute(anyString())).thenReturn("user_httpSession");

        GenericMessage<Object> result = playerServiceImpl.logout(request);

        assertEquals(HttpStatus.OK, result.getStatus());
        verify(httpSession, times(1)).removeAttribute(any());
    }

    @Test
    public void testLogoutUserFailedWithNotLoggedIn() {
        when(httpSession.getAttribute(anyString())).thenReturn(null);

        GenericMessage<Object> result = playerServiceImpl.logout(request);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals("Logout failed, player is not logged in...", result.getMessage());
    }

    @Test
    public void testLogoutUserFailedWithException() {
        when(httpSession.getAttribute(anyString())).thenReturn("user_httpSession");
        doThrow(new RuntimeException("login failed")).when(httpSession).removeAttribute(any());

        GenericMessage<Object> result = playerServiceImpl.logout(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatus());
        assertEquals("Logout failed, please try again...", result.getMessage());
    }

    @Test
    public void testSetLocationSuccess() throws JsonProcessingException {
        PlayerDto playerDto = PlayerDto.builder().username("username").build();
        String playerObj = objectMapper.writeValueAsString(playerDto);
        when(httpSession.getAttribute(any())).thenReturn(playerObj);

        String location = "HAMILTON";
        PlayerSetZoneRequest playerSetZoneRequest =
                PlayerSetZoneRequest.builder().zone(location).username("username").build();

        GenericMessage<Object> result = playerServiceImpl.setZone(playerSetZoneRequest);

        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals(
                "Successfully sent set zone request as location: " + location, result.getMessage());
    }

    @Test
    public void testSetLocationFailedWithPlayerNotLoggedIn() {
        when(httpSession.getAttribute(anyString())).thenReturn(null);
        PlayerSetZoneRequest playerSetZoneRequest =
                PlayerSetZoneRequest.builder().zone("HAMILTON").username("username").build();

        GenericMessage<Object> result = playerServiceImpl.setZone(playerSetZoneRequest);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals("SetLocation failed, player is not logged in....", result.getMessage());
    }
}
