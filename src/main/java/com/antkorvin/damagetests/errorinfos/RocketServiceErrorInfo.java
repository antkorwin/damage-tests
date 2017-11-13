package com.antkorvin.damagetests.errorinfos;

/**
 * Created by Korovin Anatolii on 13.11.17.
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
public enum RocketServiceErrorInfo implements ErrorInfo {
    ROCKET_NOT_FOUND("Not Found Rocket entity by id");

    private final int BASE = 1000;
    private final String message;

    RocketServiceErrorInfo(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public Integer getCode() {
        return BASE + ordinal();
    }
}
