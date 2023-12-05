/* (C)2023 */
package com.cas.hrmservice;

import static com.cas.hrmservice.constant.Constants.HRM_ACTION_SEND_HEART_RATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.cas.hrmservice.dto.GenericMessage;
import com.cas.hrmservice.dto.HeartRateDto;
import com.cas.hrmservice.dto.HrmReceiveRequest;
import com.cas.hrmservice.dto.MessageDto;
import com.cas.hrmservice.scheduler.SendingHeartRateScheduler;
import com.cas.hrmservice.service.MessageService;
import com.cas.hrmservice.service.impl.HrmServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
public class HrmServiceTests {
    @Mock private MessageService messageService;

    @Mock private SendingHeartRateScheduler sendingHeartRateScheduler;

    private ObjectMapper objectMapper;

    @InjectMocks private HrmServiceImpl hrmService;

    private HrmReceiveRequest request;

    private String username;

    private int heartRate;

    private String heartRateMsg;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        objectMapper = new ObjectMapper();
        username = "username";
        heartRate = 100;

        request = HrmReceiveRequest.builder().username(username).heartRate(heartRate).build();

        HeartRateDto heartRateDto =
                HeartRateDto.builder().username(username).heartRate(heartRate).build();
        MessageDto messageDto =
                MessageDto.builder()
                        .action(HRM_ACTION_SEND_HEART_RATE)
                        .heartRateDto(heartRateDto)
                        .build();
        heartRateMsg = objectMapper.writeValueAsString(messageDto);
    }

    @Test
    public void testSendstartHrmSuccess() {
        hrmService.startHrm(username);
        verify(sendingHeartRateScheduler, times(1)).start(username);
    }

    @Test
    public void testSendStopHrmSuccess() {
        hrmService.stopHrm(username);
        verify(sendingHeartRateScheduler, times(1)).stop(username);
    }

    @Test
    public void testSendHeartRateSuccess() throws JsonProcessingException {
        hrmService.sendHeartRate(username, heartRate);
        verify(messageService, times(1)).sendMessage(any(), any(), eq(heartRateMsg));
    }

    /** Transmit heart rate test */
    @Test
    public void testTransmitHeartRateSuccess() {
        GenericMessage<HeartRateDto> result = hrmService.transmitHeartRate(request);

        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("successfully sent heart rate to game service...", result.getMessage());
    }

    @Test
    public void testTransmitHeartRateFailedWithBelowMinHr() {
        request.setHeartRate(-20);
        GenericMessage<HeartRateDto> result = hrmService.transmitHeartRate(request);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals("heart rate is not valid, please double check...", result.getMessage());
    }

    @Test
    public void testTransmitHeartRateFailedWithAboveMaxHr() {
        request.setHeartRate(500);
        GenericMessage<HeartRateDto> result = hrmService.transmitHeartRate(request);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals("heart rate is not valid, please double check...", result.getMessage());
    }
}
