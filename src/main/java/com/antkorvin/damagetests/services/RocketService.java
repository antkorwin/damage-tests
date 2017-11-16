package com.antkorvin.damagetests.services;


import com.antkorvin.damagetests.models.Rocket;

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
     * Fire rocket, if its not dummy
     * @param id rocket id
     * @return true if success
     */
    void fire(UUID id);
}
