package com.antkorvin.damagetests.repositories;

import com.antkorvin.damagetests.models.Rocket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Created by Korovin Anatolii on 11.11.17.
 *
 * DAO level for entity Rocket
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
public interface RocketRepository extends JpaRepository<Rocket, UUID>{
}
