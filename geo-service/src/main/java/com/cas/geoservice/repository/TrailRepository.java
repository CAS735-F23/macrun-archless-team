/* (C)2023 */
package com.cas.geoservice.repository;

import com.cas.geoservice.entity.Trail;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrailRepository extends JpaRepository<Trail, Long> {

  Optional<Trail> findByZone(String zone);
}
