/* (C)2023 */
package com.cas.hrmservice.controller;

import com.cas.hrmservice.dto.GenericMessage;
import com.cas.hrmservice.dto.HeartRateDto;
import com.cas.hrmservice.dto.HrmReceiveRequest;
import com.cas.hrmservice.service.HrmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "hrm")
public class HrmController {
    private final HrmService hrmService;

    @Autowired
    public HrmController(HrmService hrmService) {
        this.hrmService = hrmService;
    }

    @PostMapping("/transmit")
    public ResponseEntity<GenericMessage<HeartRateDto>> transmitHeartRate(
            @RequestBody HrmReceiveRequest request) {
        GenericMessage<HeartRateDto> response = hrmService.transmitHeartRate(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
