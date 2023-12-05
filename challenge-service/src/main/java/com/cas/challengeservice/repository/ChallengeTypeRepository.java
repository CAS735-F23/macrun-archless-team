package com.cas.challengeservice.repository;

import com.cas.challengeservice.entity.ChallengeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeTypeRepository extends JpaRepository<ChallengeType, Long> {
    Optional<ChallengeType> findByDescriptionAndUserHeartRateAndExerciseCount(
            String description, Long userHeartRate, Long exerciseCount);

    Optional<ChallengeType> findByDescriptionAndUserHeartRate(
            String description, Long userHeartRate);

    Optional<ChallengeType> findByDescription(String description);
}
