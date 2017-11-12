package com.antkorvin.damagetests.mappers;

import com.antkorvin.damagetests.api.dto.RocketDTO;
import com.antkorvin.damagetests.models.Rocket;
import org.mapstruct.Mapper;

/**
 * Created by Korovin Anatolii on 12.11.17.
 *
 * Mapper: Rocket -> RocketDTO
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface RocketMapper {

    RocketDTO toDto(Rocket rocket);
}
