package com.cas.challengeservice.repository;

import com.cas.challengeservice.entity.ChallengeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChallengeTypeRepository extends JpaRepository<ChallengeType, Long> {
    Optional<ChallengeType> findByDescriptionAndUserHeartRateAndExerciseCount(String description, Long userHeartRate, Long exerciseCount);
    Optional<ChallengeType> findByDescriptionAndUserHeartRate(String description, Long userHeartRate);
}