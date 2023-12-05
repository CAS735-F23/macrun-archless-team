package com.cas.challengeservice.controller;

import com.cas.challengeservice.dto.*;
import com.cas.challengeservice.service.BadgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/badge")
public class BadgeController {
    private final BadgeService badgeService;

    @Autowired
    public BadgeController(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    @GetMapping("/get-badge-list")
    public ResponseEntity<GenericMessage<List<BadgeDto>>> getBadgeList(
            @RequestParam String challenge, @RequestParam String username) {
        BadgeGetRequest request = new BadgeGetRequest(challenge, username);
        GenericMessage<List<BadgeDto>> response = badgeService.getBadgeList(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/add-badge")
    public ResponseEntity<GenericMessage<BadgeDto>> addBadge(
            @RequestBody BadgeAddRequest request) {
        GenericMessage<BadgeDto> response = badgeService.addBadge(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/delete-badge")
    public ResponseEntity<GenericMessage<BadgeDto>> deleteBadge(
            @RequestBody BadgeDeleteRequest request) {
        GenericMessage<BadgeDto> response = badgeService.deleteBadge(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}