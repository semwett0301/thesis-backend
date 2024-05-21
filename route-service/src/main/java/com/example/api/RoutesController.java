package com.example.api;

import com.example.model.dto.request.RouteRequest;
import com.example.model.dto.response.RouteResponse;
import com.example.model.dto.response.SavedRoutesResponse;
import com.example.model.exceptions.GptNotWorkingException;
import com.example.security.model.AuthenticationToken;
import com.example.services.RouteService.RouteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/routes")
@AllArgsConstructor
public class RoutesController {
    private final RouteService routeService;

    @PostMapping
    public RouteResponse createRoute(AuthenticationToken auth, @Valid @RequestBody RouteRequest routeRequest) throws GptNotWorkingException {
        var username = auth.getPrincipal();

        return username.equals("") ? routeService.createRoute(routeRequest)
                : routeService.createRoute(routeRequest, username);
    }

    @PostMapping("/saved")
    public SavedRoutesResponse getSavedRoutes() {
        return routeService.getRoutes();
    }

    @PostMapping("/{id}/save")
    public RouteResponse saveRoute(@PathVariable UUID id) {
        return routeService.saveRoute(id);
    }
}
