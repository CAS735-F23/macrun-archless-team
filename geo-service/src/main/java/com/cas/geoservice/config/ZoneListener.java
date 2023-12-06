package com.cas.geoservice.config;

import com.cas.geoservice.dto.MessageDto;
import com.cas.geoservice.dto.PlayerSetZoneRequest;
import com.cas.geoservice.entity.PlayerZone;
import com.cas.geoservice.repository.PlayerZoneRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.cas.geoservice.constant.Constants.MQ_REQUEST_SET_ZONE;

@Component
@Log4j2
public class ZoneListener implements MessageListener {
    private PlayerZoneRepository playerZoneRepository;

    private ObjectMapper objectMapper;

    @Autowired
    public ZoneListener(PlayerZoneRepository playerZoneRepository) {
        this.playerZoneRepository = playerZoneRepository;
        objectMapper = new ObjectMapper();
    }

    @Override
    public void onMessage(Message message) {
        try {
            String messageBody = new String(message.getBody());
            log.info("Received message: " + messageBody);

            MessageDto messageDto = objectMapper.readValue(messageBody, MessageDto.class);

            switch (messageDto.getAction()) {
                case MQ_REQUEST_SET_ZONE:
                    log.info("MQ - Set zone: {}", messageDto.getPlayerSetZoneRequest());
                    PlayerSetZoneRequest request = messageDto.getPlayerSetZoneRequest();
                    PlayerZone playerZone = PlayerZone.builder().username(request.getUsername()).name(request.getZone()).build();

                    PlayerZone zone = playerZoneRepository.findByUsername(request.getUsername());
                    if(Objects.isNull(zone)) {
                        log.info("Save player {} zone to : {}", playerZone.getUsername(), playerZone.getName());
                        playerZoneRepository.save(playerZone);
                    } else {
                        log.info("Update player {} zone to : {}", zone.getUsername(), zone.getName());
                        zone.setName(request.getZone());
                        playerZoneRepository.save(zone);
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
