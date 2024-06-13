package com.example.model.dto.internal;

import com.example.CityDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class TicketsInternalRequest {
    private Date startDate;
    private Date endDate;
    private String startCity;
    private String startCityIata;
    private String endCity;
    private String endCityIata;
    private Integer maxPrice;
}
