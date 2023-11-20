package com.cas.challengeservice.repository;

import com.cas.challengeservice.entity.Challenge;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, String> {
    Optional<Challenge> findByUserId(String userId);
}