package com.cas.geoservice.controller;

import com.cas.geoservice.dto.TrailDto;
import com.cas.geoservice.service.TrailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/geo")
public class TrailController {
    private final TrailService trailService;

    @Autowired
    public TrailController(TrailService trailService) {
        this.trailService = trailService;
    }

    @GetMapping("/trail")
    public ResponseEntity<TrailDto> getTrail(@RequestParam String zone) {
        TrailDto trailDto = trailService.getTrail(zone);
        if (trailDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(trailDto);
    }
}