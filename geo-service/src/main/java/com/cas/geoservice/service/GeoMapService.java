package com.cas.geoservice.service;

import com.cas.geoservice.dto.*;

public interface GeoMapService {
    GenericMessage<GeoMapDto> getGeoMap(GeoMapGetRequest request);
//    GenericMessage<GeoMapDto> setLocation(SetLocationRequest request);
}
