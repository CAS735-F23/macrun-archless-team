package com.cas.geoservice.service.Impl;

import com.cas.geoservice.dto.*;
import com.cas.geoservice.entity.PlayerZone;
import com.cas.geoservice.entity.Trail;
import com.cas.geoservice.repository.PlayerZoneRepository;
import com.cas.geoservice.repository.TrailRepository;
import com.cas.geoservice.service.TrailService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Log4j2
public class TrailServiceImpl implements TrailService {
    private final TrailRepository trailRepository;

    private final PlayerZoneRepository playerZoneRepository;

    @Autowired
    public TrailServiceImpl(TrailRepository trailRepository, PlayerZoneRepository playerZoneRepository) {
        this.trailRepository = trailRepository;
        this.playerZoneRepository = playerZoneRepository;
    }

    @Override
    public GenericMessage<TrailDto> getTrail(TrailGetRequest request) {
        PlayerZone playerZone = playerZoneRepository.findByUsername(request.getUsername());
        log.info("fetching playzone for {}", request.getUsername());

        if (Objects.isNull(playerZone)) {
            return GenericMessage.<TrailDto>builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("The player hasn't set zone, please set zone first...")
                    .data(null)
                    .build();
        }

        Optional<Trail> matchingTrail = trailRepository.findByZone(playerZone.getName());

        if (matchingTrail.isPresent()) {
            Trail trail = matchingTrail.get();
            return GenericMessage.<TrailDto>builder()
                    .status(HttpStatus.OK)
                    .message("Trail found successfully, and returned")
                    .data(trail.toDto())
                    .build();
        } else {
            return GenericMessage.<TrailDto>builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("No trail associated with this zone")
                    .build();
        }
    }
}
