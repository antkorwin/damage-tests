package com.antkorvin.damagetests.scheduler;

import com.antkorvin.damagetests.services.RocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by Korovin Anatolii on 17.11.17.
 *
 * Rocket recharge async event listener
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
@Component
public class RocketRechargeListener {

    private final RocketService rocketService;

    @Autowired
    public RocketRechargeListener(RocketService rocketService) {
        this.rocketService = rocketService;
    }

    @Async
    @EventListener
    public void onRocketRechargeEvent(RocketRechargeEvent event){
        rocketService.charge(event.getRocket().getId());
    }
}
