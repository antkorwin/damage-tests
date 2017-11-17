package com.antkorvin.damagetests.scheduler;

import com.antkorvin.damagetests.models.Rocket;
import com.antkorvin.damagetests.models.RocketStatus;
import com.antkorvin.damagetests.services.RocketService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.Mockito.*;

/**
 * Created by Korovin Anatolii on 17.11.17.
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
public class RocketRechargeSchedulerTest {

    private RocketRechargeScheduler rocketRecharger;

    @Mock
    private RocketService rocketService;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        rocketRecharger = new RocketRechargeScheduler(rocketService, applicationEventPublisher);
    }

    @Test
    public void chargeSchedulerTest() {

        // Arrange
        Rocket rocket1 = getRocketMock();
        Rocket rocket2 = getRocketMock();

        when(rocketService.getAllByStatus(RocketStatus.USED))
                .thenReturn(Arrays.asList(rocket1, rocket2));

        ArgumentCaptor<RocketRechargeEvent> captor = ArgumentCaptor.forClass(RocketRechargeEvent.class);

        // Act
        rocketRecharger.charge();

        // Asserts
        verify(applicationEventPublisher, times(2))
                .publishEvent(captor.capture());

        Assertions.assertThat(captor.getAllValues())
                  .hasSize(2)
                  .extracting(RocketRechargeEvent::getRocket)
                  .contains(rocket1, rocket2);
    }


    private Rocket getRocketMock() {
        Rocket rocket = mock(Rocket.class);
        when(rocket.getId()).thenReturn(UUID.randomUUID());
        return rocket;
    }
}