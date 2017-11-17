package com.antkorvin.damagetests.scheduler;

import com.antkorvin.damagetests.models.Rocket;
import com.antkorvin.damagetests.services.RocketService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Korovin Anatolii on 17.11.17.
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
public class RocketRechargeListenerTest {

    @Mock
    private RocketService rocketService;

    private RocketRechargeListener listener;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        listener = new RocketRechargeListener(rocketService);
    }

    @Test
    public void onRocketRechargeEvent() {
        // Arrange
        UUID id = UUID.randomUUID();
        Rocket rocket = mock(Rocket.class);
        when(rocket.getId()).thenReturn(id);
        RocketRechargeEvent event = new RocketRechargeEvent(rocket);

        // Act
        listener.onRocketRechargeEvent(event);

        // Asserts
        verify(rocketService).charge(eq(id));
    }
}