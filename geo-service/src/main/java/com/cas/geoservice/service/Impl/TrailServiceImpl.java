package com.cas.geoservice.service.Impl;

import com.cas.geoservice.dto.CoordinateDto;
import com.cas.geoservice.dto.PlaceDto;
import com.cas.geoservice.dto.TrailDto;
import com.cas.geoservice.entity.Coordinate;
import com.cas.geoservice.entity.Place;
import com.cas.geoservice.entity.Trail;
import com.cas.geoservice.repository.TrailRepository;
import com.cas.geoservice.service.TrailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrailServiceImpl implements TrailService {
    private final TrailRepository trailRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    public TrailServiceImpl(TrailRepository trailRepository, ModelMapper modelMapper) {
        this.trailRepository = trailRepository;
    }

//    @Override
//    public TrailDto createTrail(TrailDto trailDto) {
//        Trail trail = new Trail();
//        trail.setZone(trailDto.getZone());
//
//        List<Coordinate> path = trailDto.getPath().stream().map(coordinateDto -> {
//            Coordinate coordinate = new Coordinate();
//            coordinate.setX(coordinateDto.getX());
//            coordinate.setY(coordinateDto.getY());
//            coordinate.setTrail(trail);
//            return coordinate;
//        }).collect(Collectors.toList());
//        trail.setPath(path);
//
//        List<Place> places = trailDto.getPlaces().stream().map(placeDto -> {
//            Place place = new Place();
//            place.setName(placeDto.getName());
//            place.setType(placeDto.getType());
//            place.setTrail(trail);
//
//            CoordinateDto coordinateDto = placeDto.getCoordinate();
//            Coordinate coordinate = new Coordinate();
//            coordinate.setX(coordinateDto.getX());
//            coordinate.setY(coordinateDto.getY());
//            coordinate.setTrail(trail);
//            place.setCoordinate(coordinate);
//
//            return place;
//        }).collect(Collectors.toList());
//        trail.setPlaces(places);
//
//        trailRepository.save(trail);
//
//        return trailDto;
//    }
    @Override
    public TrailDto getTrail(String zone) {
//        Trail trail = trailRepository.findByZone(zone);
//        if (trail == null) {
//            return null;
//        }
//
//        TrailDto trailDto = modelMapper.map(trail, TrailDto.class);
//
//        // Map each Coordinate to a CoordinateDto
//        List<CoordinateDto> coordinateDtoList = trail.getPath().stream()
//                .map(coordinate -> modelMapper.map(coordinate, CoordinateDto.class))
//                .collect(Collectors.toList());
//        trailDto.setPath(coordinateDtoList);
//
//        return trailDto;
        // Hardcoded path coordinates
        // Hardcoded path coordinates

        List<PlaceDto> path = Arrays.asList(
                new PlaceDto(1L, "shelter1", new CoordinateDto(2L, 14.0, 3.0), "shelter"),
                new PlaceDto(2L, "shelter2", new CoordinateDto(3L, 18.0, 6.0), "shelter"),
                new PlaceDto(3L, "shelter3", new CoordinateDto(4L, 14.0, 11.0), "shelter"),
                new PlaceDto(4L, "shelter4", new CoordinateDto(5L, 45.0, 36.0), "shelter"),
                new PlaceDto(5L, "shelter5", new CoordinateDto(6L, 7.0, 89.0), "shelter")
        );

        // Hardcoded places
        List<PlaceDto> places = path;

        return new TrailDto(1L, zone, path, places);
    }
}
