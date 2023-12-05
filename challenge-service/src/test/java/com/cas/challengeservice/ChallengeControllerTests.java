package com.cas.challengeservice;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.cas.challengeservice.controller.ChallengeController;
import com.cas.challengeservice.dto.*;
import com.cas.challengeservice.service.ChallengeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@SpringBootTest
public class ChallengeControllerTests {
    @Mock private ChallengeService challengeService;
    @InjectMocks private ChallengeController challengeController;
    private Long userHeartRate;
    private String type;

    private GenericMessage<ChallengeTypeDto> message;
    private ChallengeGetRequest challengeGetRequest;
    private ChallengeAddRequest challengeAddRequest;
    private ChallengeDeleteRequest challengeDeleteRequest;

    @BeforeEach
    public void setUp() {
        userHeartRate = 70L;
        type = "Balance";

        challengeGetRequest = new ChallengeGetRequest(userHeartRate, type);
        challengeAddRequest = new ChallengeAddRequest(); // add properties as necessary
        challengeDeleteRequest = new ChallengeDeleteRequest(); // add properties as necessary

        message = GenericMessage.<ChallengeTypeDto>builder().status(HttpStatus.OK).build();
    }

    @Test
    public void testGetChallenge() {
        when(challengeService.getChallenge(any())).thenReturn(message);

        ResponseEntity<GenericMessage<ChallengeTypeDto>> responseEntity =
                challengeController.getChallenge(userHeartRate, type);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(challengeService, times(1)).getChallenge(any());
    }

    @Test
    public void testAddChallenge() {
        when(challengeService.addChallenge(any())).thenReturn(message);

        ResponseEntity<GenericMessage<ChallengeTypeDto>> responseEntity =
                challengeController.addChallenge(challengeAddRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(challengeService, times(1)).addChallenge(any());
    }

    @Test
    public void testDeleteChallenge() {
        when(challengeService.deleteChallenge(any())).thenReturn(message);

        ResponseEntity<GenericMessage<ChallengeTypeDto>> responseEntity =
                challengeController.deleteChallenge(challengeDeleteRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(challengeService, times(1)).deleteChallenge(any());
    }
}
