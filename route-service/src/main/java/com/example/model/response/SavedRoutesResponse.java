package com.example.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class SavedRoutesResponse {
    private List<RouteResponse> recently_routes;
    private List<RouteResponse> saved_routes;
}
