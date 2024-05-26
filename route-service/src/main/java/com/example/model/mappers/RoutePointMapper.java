package com.example.model.mappers;

import com.example.model.internal.GeneratedRoutePoint;
import com.example.model.response.CoordsResponse;
import com.example.model.response.RoutePointResponse;
import com.example.model.entities.db.Route;
import com.example.model.entities.db.RoutePoint;

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
        routePointResponse.setStart_time(generatedRoutePoint.getStartTime());
        routePointResponse.setEnd_time(generatedRoutePoint.getEndTime());

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
        routePointResponse.setStart_time(routePoint.getStartTime());
        routePointResponse.setEnd_time(routePoint.getEndTime());

        return routePointResponse;
    }

    public static RoutePoint createRoutePoint(RoutePointResponse routePointResponse, Route route) {
        var routePoint = new RoutePoint();

        routePoint.setName(routePointResponse.getName());
        routePoint.setDescription(routePointResponse.getDescription());
        routePoint.setDate(routePointResponse.getDate());
        routePoint.setStartTime(routePointResponse.getStart_time());
        routePoint.setEndTime(routePointResponse.getEnd_time());
        routePoint.setUrl(routePointResponse.getUrl());
        routePoint.setLatitude(routePointResponse.getCoords().getLatitude());
        routePoint.setLongitude(routePointResponse.getCoords().getLongitude());

        routePoint.setRoute(route);

        return routePoint;
    }
}
