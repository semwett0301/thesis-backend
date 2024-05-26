package com.example.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccommodationResponse {
    private Integer price;
    private Integer center_distance;
    private String name;
    private String link;
    private String source;
    private Integer rating;
    private Integer reviews_amount;
}
