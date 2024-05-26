package com.example.model.dto.external;

import lombok.Data;

import java.util.List;

@Data
public class YandexSuggestionResponse {
    private String status;
    private Results results;

    @Data
    public static class Results {
        private List<Location> locations;
    }

    @Data
    public static class Location {
        private String cityName;
        private String hotelsCount;
        private int score;
        private String fullName;
        private String countryCode;
        private String countryName;
        private List<String> iata;
        private String id;
        private LatLon location;
    }

    @Data
    public static class LatLon {
        private String lat;
        private String lon;
    }
}

