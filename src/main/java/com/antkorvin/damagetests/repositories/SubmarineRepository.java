package com.antkorvin.damagetests.repositories;

import com.antkorvin.damagetests.models.Submarine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

/**
 * Created by Korovin Anatolii on 14.11.17.
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
public interface SubmarineRepository extends JpaRepository<Submarine, UUID> {

    List<Submarine> findByPowerGreaterThan(int power);


    @Query(value = "select distinct s from Submarine s left join fetch s.rocketList where s.power > :power")
    List<Submarine> findByPowerGreaterThanFix(@Param("power") int power);
}
