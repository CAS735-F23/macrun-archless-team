/* (C)2023 */
package com.cas.challengeservice.service.Impl;

import com.cas.challengeservice.dto.*;
import com.cas.challengeservice.entity.Badge;
import com.cas.challengeservice.repository.BadgeRepository;
import com.cas.challengeservice.service.BadgeService;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class BadgeServiceImpl implements BadgeService {

  private final BadgeRepository badgeRepository;

  @Autowired
  public BadgeServiceImpl(BadgeRepository badgeRepository) {
    this.badgeRepository = badgeRepository;
  }

  @Override
  public GenericMessage<List<BadgeDto>> getBadgeList(BadgeGetRequest request) {
    List<Badge> matchingBadges =
        badgeRepository.findAllByChallengeAndUsername(
            request.getChallenge(), request.getUsername());

    if (matchingBadges.isEmpty()) {
      return GenericMessage.<List<BadgeDto>>builder()
          .status(HttpStatus.NOT_FOUND)
          .message("No badges associated with this challenge and user")
          .build();
    } else {
      // Convert list of Badge to list of BadgeDto
      List<BadgeDto> badgeDtoList =
          matchingBadges.stream().map(Badge::toDto).collect(Collectors.toList());

      return GenericMessage.<List<BadgeDto>>builder()
          .status(HttpStatus.OK)
          .message("Badges found successfully, and returned")
          .data(badgeDtoList)
          .build();
    }
  }

  @Override
  public GenericMessage<BadgeDto> addBadge(BadgeAddRequest request) {
    Optional<Badge> existingBadge =
        badgeRepository.findByChallengeAndUsernameAndBadgeName(
            request.getChallenge(), request.getUsername(), request.getBadgeName());

    if (existingBadge.isPresent()) {
      return GenericMessage.<BadgeDto>builder()
          .status(HttpStatus.CONFLICT)
          .message("Badge already exists.")
          .build();
    }

    Badge newBadge =
        new Badge(
            new Random().nextLong(),
            request.getChallenge(),
            request.getUsername(),
            request.getBadgeName());
    badgeRepository.save(newBadge);

    return GenericMessage.<BadgeDto>builder()
        .status(HttpStatus.CREATED)
        .message("Badge added successfully")
        .data(newBadge.toDto())
        .build();
  }

  @Override
  public GenericMessage<BadgeDto> deleteBadge(BadgeDeleteRequest request) {
    Optional<Badge> existingBadge =
        badgeRepository.findByChallengeAndUsernameAndBadgeName(
            request.getChallenge(), request.getUsername(), request.getBadgeName());

    if (!existingBadge.isPresent()) {
      return GenericMessage.<BadgeDto>builder()
          .status(HttpStatus.NOT_FOUND)
          .message("Badge not found")
          .build();
    }

    badgeRepository.delete(existingBadge.get());

    return GenericMessage.<BadgeDto>builder()
        .status(HttpStatus.OK)
        .message("Badge deleted successfully")
        .data(existingBadge.get().toDto())
        .build();
  }
}
