package com.cas.challengeservice.repository;

import com.cas.challengeservice.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<Badge> findAllByChallengeAndUsername(String challenge, String username);

    Optional<Badge> findByChallengeAndUsernameAndBadgeName(String challenge, String username, String badgeName);
}
