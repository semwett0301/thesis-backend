package com.example.security.model;

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