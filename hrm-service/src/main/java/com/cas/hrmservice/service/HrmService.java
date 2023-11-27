/* (C)2023 */
package com.cas.hrmservice.service;

import com.cas.hrmservice.dto.GenericMessage;
import com.cas.hrmservice.dto.HeartRateDto;
import com.cas.hrmservice.dto.HrmReceiveRequest;
import org.springframework.stereotype.Service;

@Service
public interface HrmService {
    void startHrm(String username);

    void stopHrm(String username);

    GenericMessage<HeartRateDto> transmitHeartRate(HrmReceiveRequest request);
}
