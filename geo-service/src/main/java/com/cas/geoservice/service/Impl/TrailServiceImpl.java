package com.cas.geoservice.service.Impl;

import com.cas.geoservice.dto.*;
import com.cas.geoservice.entity.Trail;
import com.cas.geoservice.repository.TrailRepository;
import com.cas.geoservice.service.TrailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class TrailServiceImpl implements TrailService {
    private final TrailRepository trailRepository;

    @Autowired
    public TrailServiceImpl(TrailRepository trailRepository) {
        this.trailRepository = trailRepository;
    }

    @Override
    public GenericMessage<TrailDto> getTrail(TrailGetRequest request) {
        Optional<Trail> matchingTrail = trailRepository.findByZone(request.getZone());

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
