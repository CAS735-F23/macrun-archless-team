/* (C)2023 */
package com.cas.geoservice.repository;

import com.cas.geoservice.entity.PlayerZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerZoneRepository extends JpaRepository<PlayerZone, Long> {

  PlayerZone findByUsername(String username);
}
