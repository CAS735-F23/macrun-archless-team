/* (C)2023 */
package com.cas.challengeservice.repository;

import com.cas.challengeservice.entity.Badge;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<Badge> findAllByChallengeAndUsername(String challenge, String username);

    Optional<Badge> findByChallengeAndUsernameAndBadgeName(
            String challenge, String username, String badgeName);
}
