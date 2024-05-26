package com.example.model.dto.response;

import com.example.CityDto;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class RouteResponse {
    private UUID id;
    private CityDto start_city;
    private CityDto end_city;
    private Date start_date;
    private Date end_date;
    private double transport_price;
    private double accommodation_price;
    private String additional_information;
    private Boolean is_saved;
    private List<RoutePointResponse> route_points;
}
