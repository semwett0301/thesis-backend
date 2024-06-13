package com.example.model.dto.request;

import com.example.CityDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RouteRequest {
    @NotNull
    private CityDto start_city;

    @NotNull
    private CityDto end_city;

    @NotNull
    private Date start_date;

    @NotNull
    private Date end_date;

    @NotNull
    @Min(5000)
    private double transport_price;

    @NotNull
    @Min(5000)
    private double accommodation_price;

    @NotNull
    private String additional_information;
}
