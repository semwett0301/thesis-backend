package com.example.services.RouteService;

import com.example.model.dto.request.RouteRequest;
import com.example.model.dto.response.RouteResponse;
import com.example.model.dto.response.SavedRoutesResponse;
import com.example.model.exceptions.GptNotWorkingException;

import java.util.UUID;

public interface RouteService {
    RouteResponse createRoute(RouteRequest routeRequest) throws GptNotWorkingException;
    RouteResponse createRoute(RouteRequest routeRequest, String username) throws GptNotWorkingException;
    SavedRoutesResponse getRoutes();
    RouteResponse saveRoute(UUID id);
}
