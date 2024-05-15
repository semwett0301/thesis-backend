package com.example.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public record TicketResponse(String logo_url, Integer price, String source,
                             String start_city, String end_city, String start_time,
                             String end_time, String start_point, String end_point,
                             String link) {
}
