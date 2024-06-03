package com.example.services.RouteService;

import com.example.model.dto.request.RouteRequest;
import com.example.model.dto.response.RouteResponse;
import com.example.model.dto.response.SavedRoutesResponse;

import java.util.UUID;

public interface RouteService {
    RouteResponse createRoute(RouteRequest routeRequest);
    RouteResponse createRoute(RouteRequest routeRequest, String username);
    RouteResponse getRoute(UUID id);
    SavedRoutesResponse getRoutes(String username);
    RouteResponse saveRoute(UUID id);
}
