/* (C)2023 */
package com.cas.geoservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.cas.geoservice.controller.TrailController;
import com.cas.geoservice.dto.*;
import com.cas.geoservice.service.TrailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class TrailControllerTests {
    @Mock private TrailService trailService;
    @InjectMocks private TrailController trailController;

    private GenericMessage<TrailDto> message;
    private String username;

    @BeforeEach
    public void setUp() {
        username = "username";
        message =
                GenericMessage.<TrailDto>builder()
                        .status(HttpStatus.OK)
                        .message("Trail Response")
                        .build();
    }

    @Test
    public void testGetTrail() {
        when(trailService.getTrail(any())).thenReturn(message);

        ResponseEntity<GenericMessage<TrailDto>> responseEntity =
                trailController.getTrail(username);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(trailService, times(1)).getTrail(any());
    }
}
