package com.example.services.GenerateService;

import com.example.model.dto.internal.GeneratedRoutePoint;
import com.example.model.dto.request.RouteRequest;
import com.example.model.exceptions.GptNotWorkingException;

import java.util.List;

public interface GenerateService {
    List<GeneratedRoutePoint> generate(RouteRequest request) throws GptNotWorkingException;
}
