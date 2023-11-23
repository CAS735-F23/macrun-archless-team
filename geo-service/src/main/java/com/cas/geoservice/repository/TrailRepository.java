package com.cas.geoservice.repository;

import com.cas.geoservice.entity.Trail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrailRepository extends JpaRepository<Trail, Long> {
    Trail findByZone(String zone);
}
