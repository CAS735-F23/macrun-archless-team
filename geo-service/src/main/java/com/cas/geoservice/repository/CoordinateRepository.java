package com.cas.geoservice.repository;

import com.cas.geoservice.entity.Coordinate;
import com.cas.geoservice.entity.Trail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {
    Coordinate findByXAndY(Double x, Double y);
}
