package com.example.model.mappers;

import com.example.model.dto.internal.GeneratedRoutePoint;
import com.example.model.dto.response.CoordsResponse;
import com.example.model.dto.response.RoutePointResponse;

import java.util.UUID;

public class RoutePointMapper {
    public static RoutePointResponse createRoutePointResponse(UUID id, GeneratedRoutePoint generatedRoutePoint) {
        var coords = new CoordsResponse(generatedRoutePoint.getLatitude(), generatedRoutePoint.getLongitude());
        var routePointResponse = new RoutePointResponse();

        routePointResponse.setId(id);
        routePointResponse.setName(generatedRoutePoint.getName());
        routePointResponse.setDescription(generatedRoutePoint.getDescription());
        routePointResponse.setCoords(coords);
        routePointResponse.setUrl(generatedRoutePoint.getUrl());
        routePointResponse.setDate(generatedRoutePoint.getDate());
        routePointResponse.setStartTime(generatedRoutePoint.getStartTime());
        routePointResponse.setEndTime(generatedRoutePoint.getEndTime());

        return routePointResponse;
    }
}
