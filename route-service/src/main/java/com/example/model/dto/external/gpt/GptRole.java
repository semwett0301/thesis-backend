package com.example.model.dto.external.gpt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum GptRole {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant");

    private final String role;

    @JsonValue
    public String getRole() {
        return role;
    }

    @JsonCreator
    public static GptRole getRoleFromString(String value) {
        for (GptRole dep : GptRole.values()) {
            if (dep.getRole().equals(value)) {
                return dep;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return role;
    }
}
