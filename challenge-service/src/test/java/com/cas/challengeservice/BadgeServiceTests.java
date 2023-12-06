package com.cas.challengeservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cas.challengeservice.dto.*;
import com.cas.challengeservice.entity.Badge;
import com.cas.challengeservice.repository.BadgeRepository;
import com.cas.challengeservice.service.Impl.BadgeServiceImpl;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BadgeServiceTests {

  @Mock
  private BadgeRepository badgeRepository;
  @InjectMocks
  private BadgeServiceImpl badgeServiceImpl;

  private BadgeGetRequest badgeGetRequest;
  private BadgeAddRequest badgeAddRequest;
  private BadgeDeleteRequest badgeDeleteRequest;

  @BeforeEach
  public void setUp() {
    badgeGetRequest = new BadgeGetRequest("challengeType", "username");
    badgeAddRequest = new BadgeAddRequest("challengeType", "username", "badgeName");
    badgeDeleteRequest = new BadgeDeleteRequest("challengeType", "username", "badgeName");
  }

  @Test
  public void testGetBadgeListWhenBadgeNotFound() {
    when(badgeRepository.findAllByChallengeAndUsername(any(), any())).thenReturn(new ArrayList<>());

    GenericMessage<List<BadgeDto>> result = badgeServiceImpl.getBadgeList(badgeGetRequest);

    assertEquals(HttpStatus.NOT_FOUND, result.getStatus());
    assertEquals("No badges associated with this challenge and user", result.getMessage());
  }

  @Test
  public void testGetBadgeListSuccess() {
    List<Badge> badges = new ArrayList<>();
    badges.add(new Badge(1L, "challenge", "username", "badgeName"));
    when(badgeRepository.findAllByChallengeAndUsername(any(), any())).thenReturn(badges);

    GenericMessage<List<BadgeDto>> result = badgeServiceImpl.getBadgeList(badgeGetRequest);

    assertEquals(HttpStatus.OK, result.getStatus());
    assertEquals("Badges found successfully, and returned", result.getMessage());
  }

  @Test
  public void testAddBadgeWhenBadgeExist() {
    Badge badge = new Badge(1L, "challenge", "username", "badgeName");
    when(badgeRepository.findByChallengeAndUsernameAndBadgeName(any(), any(), any())).thenReturn(
        Optional.of(badge));

    GenericMessage<BadgeDto> result = badgeServiceImpl.addBadge(badgeAddRequest);

    assertEquals(HttpStatus.CONFLICT, result.getStatus());
    assertEquals("Badge already exists.", result.getMessage());
  }

  @Test
  public void testAddBadgeSuccess() {
    when(badgeRepository.findByChallengeAndUsernameAndBadgeName(any(), any(), any())).thenReturn(
        Optional.empty());

    GenericMessage<BadgeDto> result = badgeServiceImpl.addBadge(badgeAddRequest);

    assertEquals(HttpStatus.CREATED, result.getStatus());
    assertEquals("Badge added successfully", result.getMessage());
  }

  @Test
  public void testDeleteBadgeWhenBadgeNotFound() {
    when(badgeRepository.findByChallengeAndUsernameAndBadgeName(any(), any(), any())).thenReturn(
        Optional.empty());

    GenericMessage<BadgeDto> result = badgeServiceImpl.deleteBadge(badgeDeleteRequest);

    assertEquals(HttpStatus.NOT_FOUND, result.getStatus());
    assertEquals("Badge not found", result.getMessage());
  }

  @Test
  public void testDeleteBadgeSuccess() {
    Badge badge = new Badge(1L, "challenge", "username", "badgeName");
    when(badgeRepository.findByChallengeAndUsernameAndBadgeName(any(), any(), any())).thenReturn(
        Optional.of(badge));

    GenericMessage<BadgeDto> result = badgeServiceImpl.deleteBadge(badgeDeleteRequest);

    assertEquals(HttpStatus.OK, result.getStatus());
    assertEquals("Badge deleted successfully", result.getMessage());
  }
}
