package com.example.model.dto.response;

import com.example.City;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class RouteResponse {
    private UUID id;
    private City start_city;
    private City end_city;
    private Date start_date;
    private Date end_date;
    private double transport_price;
    private double accommodation_price;
    private String additional_information;
    private Boolean is_saved;
    private List<RoutePointResponse> route_points;
}
