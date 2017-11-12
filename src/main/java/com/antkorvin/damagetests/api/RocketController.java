package com.antkorvin.damagetests.api;

import com.antkorvin.damagetests.api.dto.RocketDTO;
import com.antkorvin.damagetests.mappers.RocketMapper;

import com.antkorvin.damagetests.services.RocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * Created by Korovin Anatolii on 11.11.17.
 *
 * RocketService's REST API
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
@RestController
@RequestMapping(RocketController.URL)
public class RocketController {

    public static final String URL = "rockets";

    private final RocketService rocketService;
    private final RocketMapper rocketMapper;

    @Autowired
    public RocketController(RocketService rocketService, RocketMapper rocketMapper) {
        this.rocketService = rocketService;
        this.rocketMapper = rocketMapper;
    }

    /**
     * Create new rocket
     *
     * @param name rocket name
     * @return created rocket DTO
     */
    @PostMapping(value = "create", produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public RocketDTO create(@RequestParam(name = "name") String name) {
        return rocketMapper.toDto(rocketService.create(name));
    }


    /**
     * Get existing rocket
     *
     * @param id rocket identify
     * @return rocket DTO
     */
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    public RocketDTO get(@PathVariable("id") UUID id) {
        return rocketMapper.toDto(rocketService.get(id));
    }
}
