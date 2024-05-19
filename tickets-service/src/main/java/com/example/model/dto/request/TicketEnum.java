package com.example.model.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TicketEnum {
    AIRPLANE("airplane"),
    RAILWAY("railway");

    private final String text;

    TicketEnum(final String text) {
        this.text = text;
    }

    @JsonValue
    public String getText() {
        return text;
    }

    @JsonCreator
    public static TicketEnum getRoleFromString(String value) {
        for (TicketEnum dep : TicketEnum.values()) {
            if (dep.getText().equals(value)) {
                return dep;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return text;
    }
}
