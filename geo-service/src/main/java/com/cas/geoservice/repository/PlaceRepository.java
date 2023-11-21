package com.cas.geoservice.repository;

import com.cas.geoservice.entity.Place;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByName(String name);
}
