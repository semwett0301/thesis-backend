package com.example.model.mappers;

import com.example.model.dto.internal.GeneratedRoutePoint;
import com.example.model.dto.response.CoordsResponse;
import com.example.model.dto.response.RoutePointResponse;
import com.example.model.entities.RoutePoint;

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

    public static RoutePointResponse createRoutePointResponse(RoutePoint routePoint) {
        var coords = new CoordsResponse(routePoint.getLatitude(), routePoint.getLongitude());
        var routePointResponse = new RoutePointResponse();

        routePointResponse.setId(routePoint.getId());
        routePointResponse.setName(routePoint.getName());
        routePointResponse.setDescription(routePoint.getDescription());
        routePointResponse.setCoords(coords);
        routePointResponse.setUrl(routePoint.getUrl());
        routePointResponse.setDate(routePoint.getDate());
        routePointResponse.setStartTime(routePoint.getStartTime());
        routePointResponse.setEndTime(routePoint.getEndTime());

        return routePointResponse;
    }
}
