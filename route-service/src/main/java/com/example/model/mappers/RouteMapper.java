package com.example.model.mappers;

import com.example.model.dto.request.RouteRequest;
import com.example.model.dto.response.RoutePointResponse;
import com.example.model.dto.response.RouteResponse;

import java.util.List;
import java.util.UUID;

public class RouteMapper {
    public static RouteResponse createGeneratedRouteResponse(UUID id, RouteRequest routeRequest, List<RoutePointResponse> routePointResponses) {
        var routePointResponse = new RouteResponse();

        routePointResponse.setId(id);
        routePointResponse.setRoute_points(routePointResponses);

        routePointResponse.setAccommodation_price(routeRequest.getAccommodation_price());
        routePointResponse.setTransport_price(routeRequest.getAccommodation_price());
        routePointResponse.setStart_date(routeRequest.getStart_date());
        routePointResponse.setEnd_date(routeRequest.getEnd_date());
        routePointResponse.setAdditional_information(routeRequest.getAdditional_information());
        routePointResponse.setIs_saved(false);

        routePointResponse.setStart_city(routeRequest.getStart_city());
        routePointResponse.setEnd_city(routeRequest.getEnd_city());

        return routePointResponse;
    }
}
