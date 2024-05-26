package com.example.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccommodationResponse {
    private Integer price;
    private Double center_distance;
    private String name;
    private String source;
    private Double rating;
    private Integer reviews_amount;
}
