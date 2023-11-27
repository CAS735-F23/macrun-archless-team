/* (C)2023 */
package com.cas.hrmservice.scheduler;

import static com.cas.hrmservice.constant.Constants.SENDING_HEART_RATE_SCHEDULED;

import com.cas.hrmservice.dto.HrmReceiveRequest;
import com.cas.hrmservice.service.HrmService;
import java.util.ArrayList;
import java.util.Random;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class SendingHeartRateScheduler {

    private final HrmService hrmService;

    private ArrayList<String> usernames;

    @Autowired
    public SendingHeartRateScheduler(HrmService hrmService) {
        this.hrmService = hrmService;
        this.usernames = new ArrayList<>();
    }

    public void start(String username) {
        usernames.add(username);
    }

    public void stop(String username) {
        usernames.remove(username);
    }

    @Scheduled(fixedDelay = SENDING_HEART_RATE_SCHEDULED) // seconds delay
    public void sendHeartRate() {
        if (!usernames.isEmpty()) {
            for (String usrname : usernames) {
                HrmReceiveRequest request =
                        HrmReceiveRequest.builder()
                                .heartRate(new Random().nextInt(301) + 100)
                                .username(usrname)
                                .build();

                log.info("SCHEDULER - sending hrm to game service..." + request.toString());
                hrmService.transmitHeartRate(request);
            }
        }
    }
}
