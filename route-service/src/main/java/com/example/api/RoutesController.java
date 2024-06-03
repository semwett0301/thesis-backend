package com.example.api;

import com.example.model.dto.request.RouteRequest;
import com.example.model.dto.response.RouteResponse;
import com.example.model.dto.response.SavedRoutesResponse;
import com.example.services.RouteService.RouteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("/routes")
@AllArgsConstructor
public class RoutesController {
    private final RouteService routeService;

    @PostMapping
    public RouteResponse createRoute(@AuthenticationPrincipal Optional<String> username, @Valid @RequestBody RouteRequest routeRequest) {
        return username.isEmpty() ? routeService.createRoute(routeRequest) : routeService.createRoute(routeRequest, username.get());
    }

    @GetMapping("/{id}")
    public RouteResponse getRoute(@PathVariable(name = "id") UUID id) {
        return routeService.getRoute(id);
    }

    @PostMapping("/saved")
    public SavedRoutesResponse getSavedRoutes(@AuthenticationPrincipal Optional<String> username) {
        var usernameExist = username.orElseThrow(() -> new ResponseStatusException(FORBIDDEN, "Forbidden"));
        return routeService.getRoutes(usernameExist);
    }

    @PostMapping("/{id}/save")
    public RouteResponse saveRoute(@PathVariable UUID id) {
        return routeService.saveRoute(id);
    }
}
