package com.example.model.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Role {
    USER("USER");

    private final String role;

    @Override
    public String toString() {
        return role;
    }
}
