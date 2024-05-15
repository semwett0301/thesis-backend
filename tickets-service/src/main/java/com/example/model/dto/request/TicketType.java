package com.example.model.dto.request;

public enum TicketType {
    AIRPLANE("airplane"),
    RAILWAY("railway");

    private final String text;

    TicketType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
