package com.example.model.mappers;

import com.example.model.dto.internal.GeneratedRoutePoint;
import com.example.model.dto.response.CoordsResponse;
import com.example.model.dto.response.RoutePointResponse;
import com.example.model.entities.db.Route;
import com.example.model.entities.db.RoutePoint;

import java.util.UUID;

public class RoutePointMapper {
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

    public static RoutePoint createRoutePoint(GeneratedRoutePoint routePointResponse, Route route) {
        var routePoint = new RoutePoint();

        routePoint.setName(routePointResponse.getName());
        routePoint.setDescription(routePointResponse.getDescription());
        routePoint.setDate(routePointResponse.getDate());
        routePoint.setStartTime(routePointResponse.getStartTime());
        routePoint.setEndTime(routePointResponse.getEndTime());
        routePoint.setUrl(routePointResponse.getUrl());
        routePoint.setLatitude(routePointResponse.getLatitude());
        routePoint.setLongitude(routePointResponse.getLongitude());

        routePoint.setRoute(route);

        return routePoint;
    }
}
