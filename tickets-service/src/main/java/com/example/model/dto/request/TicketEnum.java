package com.example.model.dto.request;

public enum TicketEnum {
    AIRPLANE("airplane"),
    RAILWAY("railway");

    private final String text;

    TicketEnum(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
