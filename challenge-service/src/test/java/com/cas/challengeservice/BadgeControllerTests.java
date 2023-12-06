package com.cas.challengeservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.cas.challengeservice.controller.BadgeController;
import com.cas.challengeservice.dto.*;
import com.cas.challengeservice.service.BadgeService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class BadgeControllerTests {
  @Mock
  private BadgeService badgeService;
  @InjectMocks
  private BadgeController badgeController;

  private String challenge;
  private String username;
  private String badgeName;

  private GenericMessage<BadgeDto> message;
  private BadgeGetRequest badgeGetRequest;
  private BadgeAddRequest badgeAddRequest;
  private BadgeDeleteRequest badgeDeleteRequest;

  @BeforeEach
  public void setUp() {
    challenge = "Balance";
    username = "JohnDoe";
    badgeName = "BalanceMaster";

    badgeGetRequest = BadgeGetRequest.builder().challenge(challenge).username(username).build();
    badgeAddRequest = BadgeAddRequest.builder().challenge(challenge).username(username).badgeName(badgeName).build();
    badgeDeleteRequest = BadgeDeleteRequest.builder().challenge(challenge).username(username).build();

    message = GenericMessage.<BadgeDto>builder()
        .status(HttpStatus.OK)
        .message("Badge Response")
        .build();
  }

  @Test
  public void testGetBadgeList() {
    when(badgeService.getBadgeList(any())).thenReturn(new GenericMessage<List<BadgeDto>>(HttpStatus.OK, "Badge List Response", new ArrayList<>()));

    ResponseEntity<GenericMessage<List<BadgeDto>>> responseEntity =
        badgeController.getBadgeList(challenge, username);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    verify(badgeService, times(1)).getBadgeList(any());
  }

  @Test
  public void testAddBadge() {
    when(badgeService.addBadge(any())).thenReturn(message);

    ResponseEntity<GenericMessage<BadgeDto>> responseEntity =
        badgeController.addBadge(badgeAddRequest);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    verify(badgeService, times(1)).addBadge(any());
  }

  @Test
  public void testDeleteBadge() {
    when(badgeService.deleteBadge(any())).thenReturn(message);

    ResponseEntity<GenericMessage<BadgeDto>> responseEntity =
        badgeController.deleteBadge(badgeDeleteRequest);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    verify(badgeService, times(1)).deleteBadge(any());
  }
}
