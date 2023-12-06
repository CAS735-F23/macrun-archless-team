package com.cas.challengeservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cas.challengeservice.dto.*;
import com.cas.challengeservice.entity.ChallengeType;
import com.cas.challengeservice.repository.ChallengeTypeRepository;
import com.cas.challengeservice.service.Impl.ChallengeServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
public class ChallengeServiceTests {

  @Mock
  private ChallengeTypeRepository challengeTypeRepository;
  @InjectMocks
  private ChallengeServiceImpl challengeServiceImpl;
  private ChallengeAddRequest challengeAddRequest;
  private ChallengeDeleteRequest challengeDeleteRequest;
  private ChallengeGetRequest challengeGetRequest;

  @BeforeEach
  public void setUp() {
    challengeGetRequest = new ChallengeGetRequest(70L, "challengeType");
    challengeAddRequest = new ChallengeAddRequest(70L, 10L, "testAddChallenge");
    challengeDeleteRequest = new ChallengeDeleteRequest("testDeleteChallenge");
  }

  @Test
  public void testGetChallengeWhenChallengeTypeNotExist() {
    when(challengeTypeRepository.findByDescription(any())).thenReturn(Optional.empty());

    GenericMessage<ChallengeTypeDto> result = challengeServiceImpl.getChallenge(
        challengeGetRequest);

    assertEquals(HttpStatus.NOT_FOUND, result.getStatus());
    assertEquals(
        "GetChallenge failed, no challenge type found with this type", result.getMessage());
  }

  @Test
  public void testGetChallengeWhenUserHeartRateNotInRange() {
    ChallengeGetRequest request = new ChallengeGetRequest(300L, "Muscle");

    ChallengeType challengeType =
        ChallengeType.builder().description("Muscle").userHeartRate(200L).build();

    when(challengeTypeRepository.findByDescription(any()))
        .thenReturn(Optional.ofNullable(challengeType));

    GenericMessage<ChallengeTypeDto> result = challengeServiceImpl.getChallenge(request);

    assertEquals(HttpStatus.UNAUTHORIZED, result.getStatus());
    assertEquals(
        "GetChallenge failed, user heart rate not in range for this challenge type",
        result.getMessage());
  }

  @Test
  public void testGetChallengeSuccess() {
    ChallengeType challengeType =
        ChallengeType.builder().description("Cardio").userHeartRate(120L).build();
    ChallengeGetRequest request = new ChallengeGetRequest(120L, "Cardio");

    when(challengeTypeRepository.findByDescription(any()))
        .thenReturn(Optional.ofNullable(challengeType));

    GenericMessage<ChallengeTypeDto> result = challengeServiceImpl.getChallenge(request);

    assertEquals(HttpStatus.OK, result.getStatus());
    assertEquals("Challenge type found successfully, and returned", result.getMessage());
  }

  @Test
  public void testAddChallengeTypeWhenChallengeTypeExist() {
    ChallengeType challengeType =
        ChallengeType.builder().description("Cardio").userHeartRate(120L).build();
    ChallengeAddRequest request = new ChallengeAddRequest(120L, 30L, "Cardio");

    when(challengeTypeRepository.findByDescription(any()))
        .thenReturn(Optional.ofNullable(challengeType));

    GenericMessage<ChallengeTypeDto> result = challengeServiceImpl.addChallenge(request);

    assertEquals(HttpStatus.CONFLICT, result.getStatus());
    assertEquals("Challenge type already exists.", result.getMessage());
  }

  @Test
  public void testAddChallengeTypeSuccess() {
    when(challengeTypeRepository.findByDescription(any())).thenReturn(Optional.empty());

    GenericMessage<ChallengeTypeDto> result = challengeServiceImpl.addChallenge(
        challengeAddRequest);

    assertEquals(HttpStatus.CREATED, result.getStatus());
    assertEquals("Challenge type added successfully", result.getMessage());
  }

  @Test
  public void testDeleteChallengeTypeWhenChallengeTypeNotFound() {

    when(challengeTypeRepository.findByDescription(any())).thenReturn(Optional.empty());

    GenericMessage<ChallengeTypeDto> result = challengeServiceImpl.deleteChallenge(
        challengeDeleteRequest);

    assertEquals(HttpStatus.NOT_FOUND, result.getStatus());
    assertEquals(
        "DeleteChallenge failed, no challenge type found with this type",
        result.getMessage());
  }

  @Test
  public void testDeleteChallengeTypeSuccess() {
    ChallengeType challengeType = ChallengeType.builder().description("Flexibility").build();
    ChallengeDeleteRequest request = new ChallengeDeleteRequest("Flexibility");
    when(challengeTypeRepository.findByDescription(any()))
        .thenReturn(Optional.ofNullable(challengeType));

    GenericMessage<ChallengeTypeDto> result = challengeServiceImpl.deleteChallenge(request);

    assertEquals(HttpStatus.OK, result.getStatus());
    assertEquals("Challenge type deleted successfully", result.getMessage());
  }
}
