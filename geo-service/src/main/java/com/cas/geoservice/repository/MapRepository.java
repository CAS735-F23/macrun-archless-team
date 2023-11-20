package com.cas.geoservice.repository;

import com.cas.geoservice.entity.Map;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MapRepository extends JpaRepository<Map, Long> {
}
