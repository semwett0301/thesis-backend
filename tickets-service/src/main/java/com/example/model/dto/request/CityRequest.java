package com.example.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CityRequest {
    private String iata;
    private String title;
}
