package com.antkorvin.damagetests.services;


import com.antkorvin.damagetests.models.Rocket;
import com.antkorvin.damagetests.models.RocketStatus;

import java.util.List;
import java.util.UUID;

/**
 * Created by Korovin Anatolii on 11.11.17.
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
public interface RocketService {

    /**
     * Create new Rocket
     *
     * @param name name for new rocket
     * @return created rocket
     */
    Rocket create(String name);

    /**
     * Get existing rocket
     *
     * @param id rocket id
     * @return found rocket
     */
    Rocket get(UUID id);

    /**
     * Fire rocket
     *
     * @param id rocket id
     */
    void fire(UUID id);

    /**
     * Get all rocket with selected status
     *
     * @param status RocketStatus
     * @return list of relevant Rocket
     */
    List<Rocket> getAllByStatus(RocketStatus status);

    /**
     * Recharge rocket if its USED
     *
     * @param id roket id
     * @return updated rocket
     */
    Rocket charge(UUID id);
}
