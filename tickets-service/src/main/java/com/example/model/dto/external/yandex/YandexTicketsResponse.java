package com.example.model.dto.external.yandex;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class YandexTicketsResponse {
    private Search search;
    private List<Segment> segments;
    private List<Object> intervalSegments;
    private Pagination pagination;

    @Data
     public static class Search {
        private FromTo from;
        private FromTo to;
        private String date;
    }

    @Data
    public static class FromTo {
        private String type;
        private String title;
        private String shortTitle;
        private String popularTitle;
        private String code;
    }

    @Data
    public static class Segment {
        private ThreadInfo thread;
        private String stops;
        private Station from;
        private Station to;
        private String departure_platform;
        private String arrival_platform;
        private String departure_terminal;
        private String arrival_terminal;
        private int duration;
        private boolean has_transfers;
        private TicketsInfo tickets_info;
        private Date departure;
        private Date arrival;
        private String start_date;
    }

    @Data
    public static class ThreadInfo {
        private String number;
        private String title;
        private String shortTitle;
        private String expressType;
        private String transportType;
        private Carrier carrier;
        private String uid;
        private String vehicle;
        private TransportSubtype transportSubtype;
        private String threadMethodLink;
    }

    @Data
    public static class Carrier {
        private int code;
        private String title;
        private Codes codes;
        private String address;
        private String url;
        private String email;
        private String contacts;
        private String phone;
        private String logo;
        private String logoSvg;
    }

    @Data
    public static class Codes {
        private String sirena;
        private String iata;
        private String icao;
    }

    @Data
    public static class TransportSubtype {
        private String title;
        private String code;
        private String color;
    }

    @Data
    public static class Station {
        private String type;
        private String title;
        private String short_title;
        private String popularTitle;
        private String code;
        private String stationType;
        private String stationTypeName;
        private String transportType;
    }

    @Data
    public static class TicketsInfo {
        private boolean etMarker;
        private List<Object> places;
    }

    @Data
    public static class Pagination {
        private int total;
        private int limit;
        private int offset;
    }
}

