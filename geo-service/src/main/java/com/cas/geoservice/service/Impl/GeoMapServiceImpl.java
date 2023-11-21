package com.cas.geoservice.service.Impl;

import com.cas.geoservice.dto.GenericMessage;
import com.cas.geoservice.dto.GeoMapDto;
import com.cas.geoservice.dto.GeoMapGetRequest;
import com.cas.geoservice.dto.SetLocationRequest;
import com.cas.geoservice.entity.GeoMap;
import com.cas.geoservice.repository.GeoMapRepository;
import com.cas.geoservice.service.GeoMapService;
import com.cas.geoservice.service.MessageService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Log4j2
public class GeoMapServiceImpl implements GeoMapService {
    @Value("${spring.rabbitmq.queue}")
    private String queueName;
    private final GeoMapRepository geoMapRepository;
    private final MessageService messageService;

    @Autowired
    public GeoMapServiceImpl(
            GeoMapRepository geoMapRepository,
            MessageService messageService,
            HttpSession httpSession) {
        this.geoMapRepository = geoMapRepository;
        this.messageService = messageService;
    }

    /**
     * get geo map
     *
     * @param request getGeoMap request playload
     * @return 200 if getGeoMap successful, 401 if map id not found
     */
    @Override
    public GenericMessage<GeoMapDto> getGeoMap(GeoMapGetRequest request) {
        GeoMap geoMap = geoMapRepository.findById(request.getGeoMapId()).orElse(null);
        GenericMessage<GeoMapDto> response;

        if (Objects.isNull(geoMap)) {
            response =
                    GenericMessage.<GeoMapDto>builder()
                            .status(HttpStatus.NOT_ACCEPTABLE)
                            .message("No map assoicated with this map id")
                            .build();
        } else {
            response =
                    GenericMessage.<GeoMapDto>builder()
                            .status(HttpStatus.OK)
                            .message("Map found successfully, and returned")
                            .data(geoMap.toDto())
                            .build();
        }
        return response;
    }
//
//    /**
//     * set location
//     *
//     * @param request setLocation request playload
//     * @return 200 if setLocation successful, 401 if x or y is not in range
//     */
//    @Override
//    public GenericMessage<GeoMapDto> setLocation(SetLocationRequest request) {
//        GeoMap geoMap = geoMapRepository.findById(request.getGeoMapId()).orElse(null);
//        GenericMessage<GeoMapDto> response;
//        if (Objects.isNull(geoMap)) {
//            response =
//                    GenericMessage.<GeoMapDto>builder()
//                            .status(HttpStatus.NOT_ACCEPTABLE)
//                            .message("No map assoicated with this map id")
//                            .build();
//        } else {
//            geoMap.setX(request.getX());
//            geoMap.setY(request.getY());
//            geoMapRepository.save(geoMap);
//            response =
//                    GenericMessage.<GeoMapDto>builder()
//                            .status(HttpStatus.OK)
//                            .message("Map found successfully, and returned")
//                            .data(geoMap.toDto())
//                            .build();
//        }
//        return response;
//    }
}
