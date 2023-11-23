package com.cas.geoservice.service.Impl;

import com.cas.geoservice.dto.TrailDto;
import com.cas.geoservice.entity.Trail;
import com.cas.geoservice.repository.TrailRepository;
import com.cas.geoservice.service.TrailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrailServiceImpl implements TrailService {
    private final TrailRepository trailRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TrailServiceImpl(TrailRepository trailRepository, ModelMapper modelMapper) {
        this.trailRepository = trailRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TrailDto getTrail(String zone) {
        Trail trail = trailRepository.findByZone(zone);
        return modelMapper.map(trail, TrailDto.class);
    }
}
