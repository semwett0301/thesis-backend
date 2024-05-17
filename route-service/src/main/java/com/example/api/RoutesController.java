package com.example.api;

import com.example.model.dto.request.RouteRequest;
import com.example.model.dto.response.RouteResponse;
import com.example.model.dto.response.SavedRoutesResponse;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/routes")
public class RoutesController {
    @PostMapping
    public RouteResponse createRoute(@RequestBody RouteRequest routeRequest) {
        return null;
    }

    @PostMapping("/saved")
    public SavedRoutesResponse getSavedRoutes() {
        return null;
    }

    @PostMapping("/{id}/save")
    public RouteResponse saveRoute(@PathVariable UUID id) {
        return null;
    }
}
