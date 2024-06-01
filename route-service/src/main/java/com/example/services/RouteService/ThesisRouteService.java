package com.example.services.RouteService;

import com.example.model.dto.request.RouteRequest;
import com.example.model.dto.response.RouteResponse;
import com.example.model.dto.response.SavedRoutesResponse;
import com.example.model.entities.db.Route;
import com.example.model.exceptions.GptNotWorkingException;
import com.example.model.mappers.RouteMapper;
import com.example.model.mappers.RoutePointMapper;
import com.example.model.utils.RouteStatus;
import com.example.repositories.db.CityRepository;
import com.example.repositories.db.RoutePointRepository;
import com.example.repositories.db.RouteRepository;
import com.example.repositories.db.UserRepository;
import com.example.services.GenerateService.GenerateService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@AllArgsConstructor
public class ThesisRouteService implements RouteService {
    private GenerateService generateService;

    private UserRepository userRepository;
    private RouteRepository routeRepository;
    private RoutePointRepository routePointRepository;
    private CityRepository cityRepository;

    @Override
    @Transactional
    public RouteResponse createRoute(RouteRequest routeRequest) throws GptNotWorkingException {
        return getRouteResponseAfterGeneration(routeRequest);
    }

    @Override
    @Transactional
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

            route.setStatus(RouteStatus.GENERATED);

            final var routeResult = routeRepository.save(route);
            routeResponse.setId(routeResult.getId());

            var routePointsEntities = routeResponse.getRoute_points().stream()
                    .map(routePointResponse -> RoutePointMapper.createRoutePoint(routePointResponse, routeResult))
                    .toList();
            routePointRepository.saveAll(routePointsEntities);
        }

        return routeResponse;
    }

    @Override
    public SavedRoutesResponse getRoutes(String username) {
        var user = userRepository.findByUsername(username);
        var routes = routeRepository.findByStartDateAfterAndUser(getYesterday(), user.orElseThrow(() -> new ResponseStatusException(FORBIDDEN, "Forbidden")));

        var routesRecently = routes.stream()
                .filter(route -> route.getStatus().equals(RouteStatus.GENERATED))
                .map(RouteMapper::createRouteResponse)
                .toList();

        var routesSaved = routes.stream()
                .filter(route -> route.getStatus().equals(RouteStatus.SAVED))
                .map(RouteMapper::createRouteResponse)
                .toList();

        return new SavedRoutesResponse(routesRecently, routesSaved);
    }

    @Override
    public RouteResponse saveRoute(UUID id) {
        var route = routeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Route wasn't found"));

        route.setStatus(RouteStatus.SAVED);
        routeRepository.save(route);

        return RouteMapper.createRouteResponse(route);
    }

    @Scheduled(cron = "${routes.clear-cron}", zone = "${routes.timezone}")
    public void clearRoutes() {
        RouteStatus[] statuses = {RouteStatus.GENERATED, RouteStatus.FAILED};
        var routesNotSaved = routeRepository.findByStatusInOrStartDateBefore(statuses, new Date());
        routeRepository.deleteAll(routesNotSaved);
        System.out.println("COMPLETE");
    }

    private RouteResponse getRouteResponseAfterGeneration(RouteRequest routeRequest) throws GptNotWorkingException {
        var generatedRoutePoints = generateService.generate(routeRequest);

        var routePointsResponse = generatedRoutePoints.stream()
                .map((route) -> RoutePointMapper.createRoutePointResponse(null, route))
                .toList();

        return RouteMapper.createGeneratedRouteResponse(null, routeRequest, routePointsResponse);

    }

    private Date getYesterday() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);

        return calendar.getTime();
    }
}
