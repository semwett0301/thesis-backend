package com.example.services.RouteService;

import com.example.model.dto.request.RouteRequest;
import com.example.model.dto.response.RouteResponse;
import com.example.model.exceptions.GptNotWorkingException;
import com.example.model.mappers.RouteMapper;
import com.example.model.mappers.RoutePointMapper;
import com.example.services.GenerateService.GenerateService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ThesisRouteService implements RouteService {
    private GenerateService generateService;

    @Override
    @Transactional
    public RouteResponse createRoute(RouteRequest routeRequest) throws GptNotWorkingException {
        var generatedRoutePoints = generateService.generate(routeRequest);

        var routePointsResponse = generatedRoutePoints.stream()
                .map((route) -> RoutePointMapper.createRoutePointResponse(null, route))
                .toList();

        return RouteMapper.createGeneratedRouteResponse(null, routeRequest, routePointsResponse);
    }
}
