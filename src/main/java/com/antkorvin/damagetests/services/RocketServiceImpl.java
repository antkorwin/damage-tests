package com.antkorvin.damagetests.services;

import com.antkorvin.damagetests.models.Rocket;
import com.antkorvin.damagetests.repositories.RocketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


/**
 * Created by Korovin Anatolii on 11.11.17.
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
@Service
public class RocketServiceImpl implements RocketService {

    private final RocketRepository rocketRepository;

    @Autowired
    public RocketServiceImpl(RocketRepository rocketRepository) {
        this.rocketRepository = rocketRepository;
    }

    @Override
    public Rocket create(String name) {
        return rocketRepository.save(Rocket.builder()
                                           .name(name)
                                           .launchCode(generateCode())
                                           .build());
    }

    @Override
    public Rocket get(UUID id) {
        return rocketRepository.findOne(id);
    }

    private String generateCode() {
        return UUID.randomUUID().toString().substring(0,5);
    }
}
