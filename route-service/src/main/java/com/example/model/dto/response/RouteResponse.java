package com.example.model.dto.response;

import com.example.CityDto;
import com.example.model.utils.RouteStatus;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class RouteResponse implements Serializable {
    private UUID id;
    private RouteStatus status;
    private RouteResponseContent content;
    private Long queue_length;

    @Data
    public static class RouteResponseContent {
        private CityDto start_city;
        private CityDto end_city;
        private Date start_date;
        private Date end_date;
        private double transport_price;
        private double accommodation_price;
        private String additional_information;
        private List<RoutePointResponse> route_points;
    }
}
