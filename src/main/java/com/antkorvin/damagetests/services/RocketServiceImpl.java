package com.antkorvin.damagetests.services;


import com.antkorvin.damagetests.errorinfos.RocketServiceErrorInfo;
import com.antkorvin.damagetests.exceptions.NotFoundException;
import com.antkorvin.damagetests.models.Rocket;
import com.antkorvin.damagetests.models.RocketStatus;
import com.antkorvin.damagetests.repositories.RocketRepository;
import com.antkorvin.damagetests.utills.Guard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.antkorvin.damagetests.errorinfos.RocketServiceErrorInfo.IMPOSSIBLE_FIRE_ROCKET_ALREADY_USED;
import static com.antkorvin.damagetests.errorinfos.RocketServiceErrorInfo.ROCKET_NOT_FOUND;
import static com.antkorvin.damagetests.models.RocketStatus.USED;


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
    @Transactional
    public Rocket create(String name) {
        return rocketRepository.save(Rocket.builder()
                                           .name(name)
                                           .launchCode(generateCode())
                                           .build());
    }

    @Override
    @Transactional(readOnly = true)
    public Rocket get(UUID id) {
        return rocketRepository.findById(id)
                               .orElseThrow(() -> new NotFoundException(ROCKET_NOT_FOUND));
    }


    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void fire(UUID id) {

        Rocket rocket = get(id);

        Guard.checkConditionValid(USED != rocket.getStatus(),
                                  IMPOSSIBLE_FIRE_ROCKET_ALREADY_USED);

        rocket.setStatus(USED);
        rocketRepository.save(rocket);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rocket> getAllByStatus(RocketStatus status) {

        return rocketRepository.findAllByStatus(status);
    }

    @Override
    public Rocket charge(UUID id) {
        Rocket rocket = get(id);
        Guard.checkConditionValid(rocket.getStatus() == USED,
                                  RocketServiceErrorInfo.WRONG_ROCKET_STATUS);

        rocket.setStatus(RocketStatus.REACTIVE);
        return rocketRepository.save(rocket);
    }


    @Override
    @Transactional(readOnly = true)
    public void readWithReadOnlyTransaction(UUID firstId, UUID secondId) {
        rocketRepository.findById(firstId);
        rocketRepository.findById(secondId);
    }



    @Override
    public void readWithoutTx(UUID firstId, UUID secondId) {
        rocketRepository.findById(firstId);
        rocketRepository.findById(secondId);
    }

    @Transactional
    @Override
    public void readWithoutReadOnly(UUID firstId, UUID secondId){
        rocketRepository.findById(firstId);
        rocketRepository.findById(secondId);
    }

    private String generateCode() {
        return UUID.randomUUID().toString().substring(0, 5);
    }
}
