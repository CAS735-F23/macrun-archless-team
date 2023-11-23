package com.cas.geoservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GeoMapRepository extends JpaRepository<GeoMap, Long> {
    Optional<GeoMap> findById(Long id);
}
