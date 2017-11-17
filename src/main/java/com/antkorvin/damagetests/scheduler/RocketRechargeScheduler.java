package com.antkorvin.damagetests.scheduler;


import com.antkorvin.damagetests.models.RocketStatus;
import com.antkorvin.damagetests.services.RocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Korovin Anatolii on 17.11.17.
 *
 * Scheduler for send event on the rockets recharge
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
@Slf4j
@Component
public class RocketRechargeScheduler {

    private final RocketService rocketService;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public RocketRechargeScheduler(RocketService rocketService, ApplicationEventPublisher publisher) {
        this.rocketService = rocketService;
        this.publisher = publisher;
    }


    @Scheduled(fixedRate = 1*60*1000)
    public void charge() {
        log.info("charge! ");
        rocketService.getAllByStatus(RocketStatus.USED)
                     .stream()
                     .map(RocketRechargeEvent::new)
                     .forEach(publisher::publishEvent);
    }
}
