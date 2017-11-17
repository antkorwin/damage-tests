package com.antkorvin.damagetests.scheduler;

import com.antkorvin.damagetests.models.Rocket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Korovin Anatolii on 17.11.17.
 *
 * SpringListener's event for processing recharge rocket
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
@Setter
@Getter
@AllArgsConstructor
public class RocketRechargeEvent {
    Rocket rocket;
}
