//package com.cas.challengeservice;
//
//import com.cas.challengeservice.dto.*;
//import com.cas.challengeservice.entity.ChallengeType;
//import com.cas.challengeservice.repository.ChallengeTypeRepository;
//import com.cas.challengeservice.service.ChallengeService;
//
//import com.cas.challengeservice.service.Impl.ChallengeServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//@SpringBootTest
//public class ChallengeServiceTests {
//    @Mock private ChallengeTypeRepository challengeTypeRepository;
//    @InjectMocks  private ChallengeServiceImpl challengeServiceImpl;
//    private ChallengeAddRequest challengeAddRequest;
//    private ChallengeDeleteRequest challengeDeleteRequest;
//    private ChallengeGetRequest challengeGetRequest;
//
//    @BeforeEach
//    public void setUp() {
//        challengeAddRequest = ChallengeAddRequest.builder()
//                .type("challengeType")
//                .userHeartRate(70L)
//                .exerciseCount(10L)
//                .build();
//
//        challengeDeleteRequest = ChallengeDeleteRequest.builder()
//                .type("challengeType")
//                .build();
//
//        challengeGetRequest = ChallengeGetRequest.builder()
//                .type("challengeType")
//                .userHeartRate(70L)
//                .build();
//    }
//
//    @Test
//    public void testGetChallengeWhenChallengeTypeNotExist() {
//        when(challengeTypeRepository.findByDescription(any())).thenReturn(Optional.empty());
//
//        GenericMessage<ChallengeTypeDto> result = challengeServiceImpl.getChallenge(challengeGetRequest);
//
//        assertEquals(HttpStatus.NOT_FOUND, result.getStatus());
//        assertEquals("No challenge type found with this type", result.getMessage());
//    }
//
//    @Test
//    public void testGetChallengeWhenUserHeartRateNotInRange() {
//        ChallengeType challengeType = new ChallengeType(1L, "challengeType", 100L, 10L);
//
//        when(challengeTypeRepository.findByDescription(any())).thenReturn(Optional.of(challengeType));
//
//        GenericMessage<ChallengeTypeDto> result = challengeServiceImpl.getChallenge(challengeGetRequest);
//
//        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatus());
//        assertEquals("User heart rate not in range for this challenge type", result.getMessage());
//    }
//
//    @Test
//    public void testGetChallengeSuccess() {
//        ChallengeType challengeType = new ChallengeType(1L, "challengeType", 70L, 10L);
//
//        when(challengeTypeRepository.findByDescription(any())).thenReturn(Optional.of(challengeType));
//
//        GenericMessage<ChallengeTypeDto> result = challengeServiceImpl.getChallenge(challengeGetRequest);
//
//        assertEquals(HttpStatus.OK, result.getStatus());
//        assertEquals("Challenge type found", result.getMessage());
//        assertEquals(challengeType.toDto(), result.getData());
//    }
//
//    @Test
//    public void testAddChallengeTypeWhenChallengeTypeExist() {
//        when(challengeTypeRepository.findByDescription(any())).thenReturn(Optional.of(new ChallengeType()));
//
//        GenericMessage<ChallengeTypeDto> result = challengeServiceImpl.addChallenge(challengeAddRequest);
//
//        assertEquals(HttpStatus.CONFLICT, result.getStatus());
//        assertEquals("Challenge type already exists with this type", result.getMessage());
//    }
//
//    @Test
//    public void testAddChallengeTypeSuccess() {
//        when(challengeTypeRepository.findByDescription(any())).thenReturn(Optional.empty());
//        when(challengeTypeRepository.save(any())).thenReturn(new ChallengeType());
//
//        GenericMessage<ChallengeTypeDto> result = challengeServiceImpl.addChallenge(challengeAddRequest);
//
//        assertEquals(HttpStatus.CREATED, result.getStatus());
//        assertEquals("Challenge type has been added successfully", result.getMessage());
//    }
//
//    @Test
//    public void testDeleteChallengeTypeWhenChallengeTypeNotFound() {
//        when(challengeTypeRepository.findByDescription(any())).thenReturn(Optional.empty());
//
//        GenericMessage<ChallengeTypeDto> result = challengeServiceImpl.deleteChallenge(challengeDeleteRequest);
//
//        assertEquals(HttpStatus.NOT_FOUND, result.getStatus());
//        assertEquals("No challenge type found with this type", result.getMessage());
//    }
//
//    @Test
//    public void testDeleteChallengeTypeSuccess() {
//        ChallengeType challengeType = new ChallengeType(1L, "challengeType", 70L, 10L);
//
//        when(challengeTypeRepository.findByDescription(any())).thenReturn(Optional.of(challengeType));
//
//        GenericMessage<ChallengeTypeDto> result = challengeServiceImpl.deleteChallenge(challengeDeleteRequest);
//
//        assertEquals(HttpStatus.OK, result.getStatus());
//        assertEquals("Challenge type has been deleted successfully", result.getMessage());
//    }
//}
