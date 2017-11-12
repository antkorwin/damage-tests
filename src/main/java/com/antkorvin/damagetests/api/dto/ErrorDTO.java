package com.antkorvin.damagetests.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Korovin Anatolii on 12.11.17.
 *
 * DTO with exception code and text message
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {
    private int code;
    private String message;
    private String stackTrace;
}
