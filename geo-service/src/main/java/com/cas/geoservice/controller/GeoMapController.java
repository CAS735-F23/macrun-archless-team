package com.cas.geoservice.controller;

import com.cas.geoservice.dto.GenericMessage;
import com.cas.geoservice.dto.GeoMapDto;
import com.cas.geoservice.dto.GeoMapGetRequest;
import com.cas.geoservice.entity.GeoMap;
import com.cas.geoservice.repository.GeoMapRepository;
import com.cas.geoservice.service.GeoMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "geo-map")
public class GeoMapController {

    private final GeoMapService geoMapService;
    @Autowired
    public GeoMapController(GeoMapService geoMapService) {
        this.geoMapService = geoMapService;
    }

    @GetMapping("/getMap")
    public ResponseEntity<GenericMessage<GeoMapDto>> getMap(
            @RequestBody GeoMapGetRequest request) {
        GenericMessage<GeoMapDto> response = geoMapService.getGeoMap(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
