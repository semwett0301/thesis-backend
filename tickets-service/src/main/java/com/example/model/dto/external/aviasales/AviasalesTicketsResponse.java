package com.example.model.dto.external.aviasales;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AviasalesTicketsResponse {
    private Boolean success;
    private List<ResponseData> data;

    @Data
    public static class ResponseData {
        private String origin;
        private String destination;
        private String originAirport;
        private String destinationAirport;
        private Integer price;
        private String airline;
        private String flightNumber;
        private Date departureAt;
        private Date returnAt;
        private Integer transfers;
        private Integer returnTransfers;
        private Integer duration;
        private Integer durationTo;
        private Integer durationBack;
        private String link;
    }
}
