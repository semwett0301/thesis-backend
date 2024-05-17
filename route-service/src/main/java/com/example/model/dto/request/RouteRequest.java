package com.example.model.dto.request;

import com.example.City;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class RouteRequest {
    private City start_city;
    private City end_city;
    private Date start_date;
    private Date end_date;
    private double transport_price;
    private double accommodation_price;
    private String additional_information;
}
