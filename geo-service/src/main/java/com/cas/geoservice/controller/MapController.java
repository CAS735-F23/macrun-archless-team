package com.cas.geoservice.controller;

import com.cas.geoservice.entity.Map;
import com.cas.geoservice.repository.MapRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MapController {

    private final MapRepository mapRepository;

    public MapController(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
    }

    @GetMapping("/map")
    public Map getMap() {
        // 只有一张地图，并且它的id是1
        return mapRepository.findById(1L).orElse(null);
    }
}
