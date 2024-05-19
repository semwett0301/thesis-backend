package com.example.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public final class TicketResponse {
    private final String logo_url;
    private final Integer price;
    private final String source;
    private final String start_city;
    private final String end_city;
    private final String start_time;
    private final String end_time;
    private final String start_point;
    private final String end_point;
    private final String link;
}
