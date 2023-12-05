/* (C)2023 */
package com.cas.hrmservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.cas.hrmservice.controller.HrmController;
import com.cas.hrmservice.dto.GenericMessage;
import com.cas.hrmservice.dto.HeartRateDto;
import com.cas.hrmservice.dto.HrmReceiveRequest;
import com.cas.hrmservice.service.HrmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class HrmControllerTests {
    @Mock private HrmService hrmService;

    @InjectMocks private HrmController hrmController;

    private String username;

    private int heartRate;

    private HrmReceiveRequest hrmReceiveRequest;

    private GenericMessage<HeartRateDto> message;

    @BeforeEach
    public void setUp() {
        username = "username";
        heartRate = 100;
        hrmReceiveRequest =
                HrmReceiveRequest.builder().username(username).heartRate(heartRate).build();

        message = GenericMessage.<HeartRateDto>builder().status(HttpStatus.OK).build();
    }

    @Test
    public void testTransmitHeartRate() {
        when(hrmService.transmitHeartRate(any())).thenReturn(message);

        ResponseEntity<GenericMessage<HeartRateDto>> responseEntity =
                hrmController.transmitHeartRate(hrmReceiveRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(hrmService, times(1)).transmitHeartRate(any());
    }

    @Test
    public void testStartHrm() {
        hrmController.startHrm(hrmReceiveRequest);

        verify(hrmService, times(1)).startHrm(username);
    }

    @Test
    public void testStopHrm() {
        hrmController.stopHrm(hrmReceiveRequest);

        verify(hrmService, times(1)).stopHrm(username);
    }
}
