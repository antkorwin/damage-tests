package com.antkorvin.damagetests.repositories;

import com.antkorvin.damagetests.models.Rocket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
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

    Optional<Rocket> findById(UUID id);

    @Query("select r from Rocket r where r.name like %?1")
    List<Rocket> findByNameLike(String name);
}
