/* (C)2023 */
package com.cas.geoservice.repository;

import com.cas.geoservice.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {}
