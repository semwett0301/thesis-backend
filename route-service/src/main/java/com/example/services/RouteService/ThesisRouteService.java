package com.example.services.RouteService;

import com.example.model.dto.request.RouteRequest;
import com.example.model.dto.response.RouteResponse;
import com.example.model.dto.response.SavedRoutesResponse;
import com.example.model.entities.db.Route;
import com.example.model.mappers.RouteMapper;
import com.example.model.utils.RouteStatus;
import com.example.repositories.db.CityRepository;
import com.example.repositories.db.RouteRepository;
import com.example.repositories.db.UserRepository;
import com.example.utils.QueueUtils.QueueUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@Service
@AllArgsConstructor
public class ThesisRouteService implements RouteService {
    private final UserRepository userRepository;
    private final RouteRepository routeRepository;
    private final CityRepository cityRepository;
    private final QueueUtils queueUtils;

    @Override
    public RouteResponse createRoute(RouteRequest routeRequest) {
        var route = saveRouteEntity(routeRequest);
        queueUtils.scheduleGenerateTask(route.getId());
        return RouteMapper.createRouteResponse(route, queueUtils.getQueueSize());
    }

    @Override
    public RouteResponse createRoute(RouteRequest routeRequest, String username) {
        var route = saveRouteEntity(routeRequest, username);
        queueUtils.scheduleGenerateTask(route.getId());
        return RouteMapper.createRouteResponse(route, queueUtils.getQueueSize());
    }

    @Override
    public RouteResponse getRoute(UUID id) {
        var route = routeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Not found"));
        return RouteMapper.createRouteResponse(route, queueUtils.getQueueSize());
    }

    @Override
    public SavedRoutesResponse getRoutes(String username) {
        var user = userRepository.findByUsername(username);
        var routes = routeRepository.findByStartDateAfterAndUser(getYesterday(), user.orElseThrow(() -> new ResponseStatusException(FORBIDDEN, "Forbidden")));

        var routesRecently = routes.stream()
                .filter(route -> route.getStatus().equals(RouteStatus.GENERATED))
                .map(route -> RouteMapper.createRouteResponse(route, queueUtils.getQueueSize()))
                .toList();

        var routesSaved = routes.stream()
                .filter(route -> route.getStatus().equals(RouteStatus.SAVED))
                .map(route -> RouteMapper.createRouteResponse(route, queueUtils.getQueueSize()))
                .toList();

        return new SavedRoutesResponse(routesRecently, routesSaved);
    }

    @Override
    public RouteResponse saveRoute(UUID id) {
        var route = routeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Route wasn't found"));

        route.setStatus(RouteStatus.SAVED);
        routeRepository.save(route);

        return RouteMapper.createRouteResponse(route, queueUtils.getQueueSize());
    }

    @Scheduled(cron = "${routes.clear-cron}", zone = "${routes.timezone}")
    public void clearRoutes() {
        var routesNotSaved = routeRepository.findByStatusNotOrStartDateBefore(RouteStatus.SAVED, new Date());
        routeRepository.deleteAll(routesNotSaved);
    }

    private Route saveRouteEntity(RouteRequest request, String username) {
        var user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(FORBIDDEN, "Forbidden"));
        var route = createRouteEntity(request);

        route.setUser(user);
        return routeRepository.save(route);
    }

    private Route saveRouteEntity(RouteRequest request) {
        var route = createRouteEntity(request);
        return routeRepository.save(route);
    }

    private Route createRouteEntity(RouteRequest routeRequest) {
        var startIata = routeRequest.getStart_city().getIata();
        var endIata = routeRequest.getEnd_city().getIata();

        var startCity = cityRepository.findById(startIata).orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Bad request"));
        var endCity = cityRepository.findById(endIata).orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Bad request"));

        var route = RouteMapper.createRouteEntity(routeRequest);

        route.setStartCity(startCity);
        route.setEndCity(endCity);
        route.setStatus(RouteStatus.CREATED);

        return route;
    }

    private Date getYesterday() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);

        return calendar.getTime();
    }
}
