package com.example.services;

import com.example.model.dto.request.RouteRequest;
import com.example.model.dto.response.RouteResponse;
import com.example.model.dto.response.SavedRoutesResponse;
import com.example.model.entities.db.Route;
import com.example.model.entities.db.UserInfo;
import com.example.model.utils.RouteStatus;
import com.example.repositories.db.CityRepository;
import com.example.repositories.db.RouteRepository;
import com.example.repositories.db.UserRepository;
import com.example.services.RouteService.ThesisRouteService;
import com.example.utils.QueueUtils.QueueUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ThesisRouteServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private QueueUtils queueUtils;

    @InjectMocks
    private ThesisRouteService thesisRouteService;

    private RouteRequest routeRequest;
    private Route route;
    private UserInfo user;

    @BeforeEach
    void setUp() {
        routeRequest = new RouteRequest();
        // Populate routeRequest with necessary data

        route = new Route();
        route.setId(UUID.randomUUID());
        route.setStatus(RouteStatus.CREATED);

        user = new UserInfo();
        user.setUsername("testuser");
    }

    @Test
    void createRoute_success() {
        when(routeRepository.save(any(Route.class))).thenReturn(route);
        doNothing().when(queueUtils).scheduleGenerateTask(any(UUID.class));

        RouteResponse response = thesisRouteService.createRoute(routeRequest);

        assertNotNull(response);
        verify(routeRepository, times(1)).save(any(Route.class));
        verify(queueUtils, times(1)).scheduleGenerateTask(route.getId());
    }

    @Test
    void createRouteWithUsername_success() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(routeRepository.save(any(Route.class))).thenReturn(route);
        doNothing().when(queueUtils).scheduleGenerateTask(any(UUID.class));

        RouteResponse response = thesisRouteService.createRoute(routeRequest, "testuser");

        assertNotNull(response);
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(routeRepository, times(1)).save(any(Route.class));
        verify(queueUtils, times(1)).scheduleGenerateTask(route.getId());
    }

    @Test
    void getRoute_success() {
        when(routeRepository.findById(any(UUID.class))).thenReturn(Optional.of(route));

        RouteResponse response = thesisRouteService.getRoute(route.getId());

        assertNotNull(response);
        verify(routeRepository, times(1)).findById(route.getId());
    }

    @Test
    void getRoute_notFound() {
        when(routeRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> thesisRouteService.getRoute(route.getId()));
        verify(routeRepository, times(1)).findById(route.getId());
    }

    @Test
    void getRoutes_success() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(routeRepository.findByStartDateAfterAndUser(any(Date.class), any(UserInfo.class))).thenReturn(List.of(route));

        SavedRoutesResponse response = thesisRouteService.getRoutes("testuser");

        assertNotNull(response);
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(routeRepository, times(1)).findByStartDateAfterAndUser(any(Date.class), any(UserInfo.class));
    }

    @Test
    void saveRoute_success() {
        when(routeRepository.findById(any(UUID.class))).thenReturn(Optional.of(route));
        when(routeRepository.save(any(Route.class))).thenReturn(route);

        RouteResponse response = thesisRouteService.saveRoute(route.getId());

        assertNotNull(response);
        assertEquals(RouteStatus.SAVED, route.getStatus());
        verify(routeRepository, times(1)).findById(route.getId());
        verify(routeRepository, times(1)).save(route);
    }

    @Test
    void saveRoute_notFound() {
        when(routeRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> thesisRouteService.saveRoute(route.getId()));
        verify(routeRepository, times(1)).findById(route.getId());
    }

    @Test
    void clearRoutes_success() {
        when(routeRepository.findByStatusNotOrStartDateBefore(any(RouteStatus.class), any(Date.class))).thenReturn(List.of(route));

        thesisRouteService.clearRoutes();

        verify(routeRepository, times(1)).deleteAll(any(List.class));
    }
}
