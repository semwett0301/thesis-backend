package com.example.services.RouteService;

import com.example.model.dto.request.RouteRequest;
import com.example.model.dto.response.RouteResponse;
import com.example.model.dto.response.SavedRoutesResponse;
import com.example.model.entities.Route;
import com.example.model.exceptions.GptNotWorkingException;
import com.example.model.mappers.RouteMapper;
import com.example.model.mappers.RoutePointMapper;
import com.example.repositories.CityRepository;
import com.example.repositories.RouteRepository;
import com.example.repositories.UserRepository;
import com.example.services.GenerateService.GenerateService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@AllArgsConstructor
public class ThesisRouteService implements RouteService {
    private GenerateService generateService;

    private UserRepository userRepository;
    private RouteRepository routeRepository;
    private CityRepository cityRepository;

    @Override
    @Transactional
    public RouteResponse createRoute(RouteRequest routeRequest) throws GptNotWorkingException {
        return getRouteResponseAfterGeneration(routeRequest);
    }

    @Override
    public RouteResponse createRoute(RouteRequest routeRequest, String username) throws GptNotWorkingException {
        var routeResponse = getRouteResponseAfterGeneration(routeRequest);

        var startIata = routeRequest.getStart_city().getIata();
        var endIata = routeRequest.getEnd_city().getIata();

        var user = userRepository.findByUsername(username);
        var startCity = cityRepository.findById(startIata);
        var endCity = cityRepository.findById(endIata);

        if (user.isPresent() && startCity.isPresent() && endCity.isPresent()) {
            var route = RouteMapper.createRouteEntity(routeRequest);

            route.setUser(user.get());
            route.setStartCity(startCity.get());
            route.setEndCity(endCity.get());

            route.setIsSaved(false);

            routeRepository.save(route);
        }

        return routeResponse;
    }

    @Override
    public SavedRoutesResponse getRoutes() {
        var routes = routeRepository.findAll();

        var routesRecently = routes.stream()
                .filter(route -> !route.getIsSaved())
                .map(RouteMapper::createRouteResponse)
                .toList();

        var routesSaved = routes.stream()
                .filter(Route::getIsSaved)
                .map(RouteMapper::createRouteResponse)
                .toList();

        return new SavedRoutesResponse(routesRecently, routesSaved);
    }

    @Override
    public RouteResponse saveRoute(UUID id) {
        var route = routeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Route wasn't found"));

        route.setIsSaved(true);
        routeRepository.save(route);

        return RouteMapper.createRouteResponse(route);
    }

    private RouteResponse getRouteResponseAfterGeneration(RouteRequest routeRequest) throws GptNotWorkingException {
        var generatedRoutePoints = generateService.generate(routeRequest);

        var routePointsResponse = generatedRoutePoints.stream()
                .map((route) -> RoutePointMapper.createRoutePointResponse(null, route))
                .toList();

        return RouteMapper.createGeneratedRouteResponse(null, routeRequest, routePointsResponse);

    }
}
