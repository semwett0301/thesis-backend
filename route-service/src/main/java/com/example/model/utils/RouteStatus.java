package com.example.model.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RouteStatus {
    PENDING("PENDING"),
    GENERATED("GENERATED"),
    FAILED("FAILED"),
    SAVED("SAVED");

    private final String status;

    @JsonCreator
    public static RouteStatus getRoleFromString(String value) {
        for (RouteStatus dep : RouteStatus.values()) {
            if (dep.toString().equals(value)) {
                return dep;
            }
        }

        return null;
    }

    @Override
    @JsonValue
    public String toString() {
        return status;
    }
}
