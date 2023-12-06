/* (C)2023 */
package com.cas.geoservice.service;

import com.cas.geoservice.dto.*;

public interface TrailService {

  GenericMessage<TrailDto> getTrail(TrailGetRequest request);
}
