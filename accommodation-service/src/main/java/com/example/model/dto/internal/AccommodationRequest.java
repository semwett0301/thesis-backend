package com.example.model.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class AccommodationRequest {
    private Date startDate;
    private Date endDate;
    private Integer maxPrice;
    private String cityIata;
}
