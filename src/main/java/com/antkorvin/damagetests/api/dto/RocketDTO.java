package com.antkorvin.damagetests.api.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Created by Korovin Anatolii on 11.11.17.
 *
 * DTO for Rocket object
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class RocketDTO {
    UUID id;
    String name;
    String launchCode;
}
