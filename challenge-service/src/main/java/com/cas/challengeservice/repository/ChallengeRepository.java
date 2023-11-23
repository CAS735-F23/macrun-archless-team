/* (C)2023 */
package com.cas.challengeservice.repository;

import com.cas.challengeservice.constants.ChallengeType;
import com.cas.challengeservice.entity.Challenge;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    Optional<Challenge> findByUserHeartRateAndType(Long userHeartRate, String type);
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM Challenge c WHERE c.challengeType = :challengeType")
    boolean existsByChallengeType(@Param("challengeType") ChallengeType challengeType);

    void deleteByChallengeType(ChallengeType challengeType);
}