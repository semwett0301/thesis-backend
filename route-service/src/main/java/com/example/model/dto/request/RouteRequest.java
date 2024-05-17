package com.example.model.dto.request;

import com.example.City;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class RouteRequest {
    @NotNull
    private City start_city;

    @NotNull
    private City end_city;

    @NotNull
    private Date start_date;

    @NotNull
    private Date end_date;

    @NotNull
    private double transport_price;

    @NotNull
    @Min(5000)
    private double accommodation_price;

    @NotNull
    @Min(5000)
    private String additional_information;
}
