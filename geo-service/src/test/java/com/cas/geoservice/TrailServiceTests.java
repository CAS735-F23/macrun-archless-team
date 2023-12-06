package com.cas.geoservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cas.geoservice.dto.GenericMessage;
import com.cas.geoservice.dto.TrailDto;
import com.cas.geoservice.dto.TrailGetRequest;
import com.cas.geoservice.entity.Place;
import com.cas.geoservice.entity.PlayerZone;
import com.cas.geoservice.entity.Trail;
import com.cas.geoservice.repository.PlayerZoneRepository;
import com.cas.geoservice.repository.TrailRepository;
import com.cas.geoservice.service.Impl.TrailServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
public class TrailServiceTests {

  @Mock
  private TrailRepository trailRepository;

  @Mock
  private PlayerZoneRepository playerZoneRepository;

  @InjectMocks
  private TrailServiceImpl trailServiceImpl;

  private TrailGetRequest trailGetRequest;

  @BeforeEach
  public void setUp() {
    trailGetRequest = new TrailGetRequest("username");
  }

  @Test
  public void testGetTrailWhenPlayerZoneNotExist() {
    when(playerZoneRepository.findByUsername(anyString())).thenReturn(null);

    GenericMessage<TrailDto> result = trailServiceImpl.getTrail(trailGetRequest);

    assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
    assertEquals(
        "The player hasn't set zone, please set zone first...", result.getMessage());
  }

  @Test
  public void testGetTrailWhenTrailNotExist() {
    PlayerZone playerZone = new PlayerZone();
    playerZone.setName("Zone1");

    when(playerZoneRepository.findByUsername(anyString())).thenReturn(playerZone);
    when(trailRepository.findByZone(anyString())).thenReturn(Optional.empty());

    GenericMessage<TrailDto> result = trailServiceImpl.getTrail(trailGetRequest);

    assertEquals(HttpStatus.NOT_FOUND, result.getStatus());
    assertEquals("No trail associated with this zone", result.getMessage());
  }

  @Test
  public void testGetTrailSuccess() {
    PlayerZone playerZone = new PlayerZone();
    playerZone.setName("Zone1");

    List<Place> places = new ArrayList<>();
    Place place1 = Place.builder().name("Place1").type("Type1").CoordinateX(1.0).CoordinateY(1.0)
        .build();
    Place place2 = Place.builder().name("Place2").type("Type2").CoordinateX(2.0).CoordinateY(2.0)
        .build();
    places.add(place1);
    places.add(place2);

    Trail trail = Trail.builder().id(1L).zone("Zone1").path(places).build();

    place1.setTrail(trail);
    place2.setTrail(trail);

    when(playerZoneRepository.findByUsername(anyString())).thenReturn(playerZone);
    when(trailRepository.findByZone(anyString())).thenReturn(Optional.ofNullable(trail));

    GenericMessage<TrailDto> result = trailServiceImpl.getTrail(trailGetRequest);

    assertEquals(HttpStatus.OK, result.getStatus());
    assertEquals("Trail found successfully, and returned", result.getMessage());

    // Additional verifications
    assertNotNull(result.getData());
    assertEquals(1L, result.getData().getId());
    assertEquals("Zone1", result.getData().getZone());
    assertArrayEquals(
        places.stream().map(Place::toDto).toArray(),
        result.getData().getPath().toArray()
    );
  }
}
