/* (C)2023 */
package com.cas.playerservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.cas.playerservice.dto.GenericMessage;
import com.cas.playerservice.dto.PlayerDto;
import com.cas.playerservice.dto.PlayerRegisterRequest;
import com.cas.playerservice.repository.PlayerRepository;
import com.cas.playerservice.service.impl.PlayerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
public class PlayerServiceTests {
    @Mock private PlayerRepository playerRepository;

    @InjectMocks private PlayerServiceImpl playerServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegisterSuccess() {
        PlayerRegisterRequest request =
                new PlayerRegisterRequest("username", "password", "email", 70, 25);
        when(playerRepository.findByUsername(request.getUsername()))
                .thenReturn(java.util.Optional.empty());

        GenericMessage<PlayerDto> result = playerServiceImpl.register(request);

        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("Player Registration successful...", result.getMessage());
    }
}
