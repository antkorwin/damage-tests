package com.antkorvin.damagetests.scheduler;

import com.antkorvin.damagetests.models.Rocket;
import com.antkorvin.damagetests.models.RocketStatus;
import com.antkorvin.damagetests.services.RocketService;
import com.antkorvin.damagetests.utils.BaseSystemIT;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Korovin Anatolii on 17.11.17.
 *
 * Check integration publishing event
 * - testing receive event after run recharge scheduler
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
public class RocketRechargeSchedulerIT extends BaseSystemIT {

    @Autowired
    @Qualifier("rocketRechargeScheduler")
    private RocketRechargeScheduler rocketRechargeScheduler;

    @MockBean
    private RocketService rocketService;

    @Autowired
    private RocketRechargeSchedulerTestConfig.TestListener testListener;

    @Test
    public void testRunScheduler() {
        // Arrange
        Rocket rocket1 = mock(Rocket.class);
        Rocket rocket2 = mock(Rocket.class);
        when(rocketService.getAllByStatus(RocketStatus.USED)).thenReturn(Arrays.asList(rocket1, rocket2));

        // Act
        rocketRechargeScheduler.charge();

        // Asserts
        Assertions.assertThat(testListener.events)
                  .hasSize(2)
                  .extracting(RocketRechargeEvent::getRocket)
                  .contains(rocket1, rocket2);
    }


    @TestConfiguration
    public static class RocketRechargeSchedulerTestConfig {

        @Component
        @Primary
        public class TestListener {

            public LinkedBlockingQueue<RocketRechargeEvent> events = new LinkedBlockingQueue<>();

            @EventListener
            public void onRocketRechargeEvent(RocketRechargeEvent event) {
                events.add(event);
            }
        }
    }
}