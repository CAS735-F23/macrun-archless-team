/* (C)2023 */
package com.cas.challengeservice.repository;

import com.cas.challengeservice.entity.Challenge;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    Optional<Challenge> findByUserHeartRate(String userHeartRate);
}