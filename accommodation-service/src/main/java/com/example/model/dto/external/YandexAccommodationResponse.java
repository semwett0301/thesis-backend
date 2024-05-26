package com.example.model.dto.external;

import lombok.Data;

import java.util.List;

@Data
public class YandexAccommodationResponse {
    private List<CheapHotel> cheaphotel;

    @Data
    public static class CheapHotel {
        private int hotel_id;
        private double distance;
        private String name;
        private int stars;
        private int rating;
        private String ty_summary;
        private String property_type;
        private List<String> hotel_type;
        private LastPriceInfo last_price_info;
        private boolean has_wifi;
    }

    @Data
    public static class LastPriceInfo {
        private int price;
        private int price_pn;
        private long insertion_time;
        private int nights;
        private SearchParams search_params;
    }

    @Data
    public static class SearchParams {
        private int adults;
        private List<Object> children;  // Assuming children can have specific data type if known
        private String check_in;
        private String check_out;
    }
}

