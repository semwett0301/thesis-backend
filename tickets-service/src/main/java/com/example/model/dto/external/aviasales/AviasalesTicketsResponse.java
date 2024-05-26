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
        private String origin_airport;
        private String destination_airport;
        private Integer price;
        private String airline;
        private String flight_number;
        private Date departure_at;
        private Date return_at;
        private Integer transfers;
        private Integer return_transfers;
        private Integer duration;
        private Integer duration_to;
        private Integer duration_back;
        private String link;
    }
}
