/* (C)2023 */
package com.cas.geoservice.controller;

import com.cas.geoservice.dto.GenericMessage;
import com.cas.geoservice.dto.TrailDto;
import com.cas.geoservice.dto.TrailGetRequest;
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
  public ResponseEntity<GenericMessage<TrailDto>> getTrail(@RequestParam String username) {
    TrailGetRequest request = new TrailGetRequest(username);
    GenericMessage<TrailDto> response = trailService.getTrail(request);
    return ResponseEntity.status(response.getStatus()).body(response);
  }
}
