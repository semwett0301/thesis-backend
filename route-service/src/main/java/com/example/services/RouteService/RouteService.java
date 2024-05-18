package com.example.services.RouteService;

import com.example.model.dto.internal.GeneratedRoutePoint;
import com.example.model.dto.request.RouteRequest;
import com.example.model.dto.response.RouteResponse;
import com.example.model.exceptions.GptNotWorkingException;

import java.util.List;

public interface RouteService {
    RouteResponse createRoute(RouteRequest routeRequest) throws GptNotWorkingException;
}
