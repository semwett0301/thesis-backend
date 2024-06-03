package com.example.model.mappers;

import com.example.model.dto.request.RouteRequest;
import com.example.model.dto.response.RoutePointResponse;
import com.example.model.dto.response.RouteResponse;
import com.example.model.entities.db.Route;

import java.util.ArrayList;
import java.util.List;

public class RouteMapper {
    public static RouteResponse createRouteResponse(Route route, Long queueLength) {
        var routeResponse = new RouteResponse();

        routeResponse.setId(route.getId());
        routeResponse.setStatus(route.getStatus());
        routeResponse.setQueue_length(queueLength);

        var routeResponseContent = new RouteResponse.RouteResponseContent();

        routeResponseContent.setStart_date(route.getStartDate());
        routeResponseContent.setEnd_date(route.getEndDate());

        var startCity = CityMapper.INSTANCE.toCityDto(route.getStartCity());
        var endCity = CityMapper.INSTANCE.toCityDto(route.getEndCity());

        routeResponseContent.setStart_city(startCity);
        routeResponseContent.setEnd_city(endCity);

        routeResponseContent.setTransport_price(route.getTransportPrice());
        routeResponseContent.setAccommodation_price(route.getAccommodationPrice());

        List<RoutePointResponse> routePoints = route.getRoutePoints() != null ?
                route.getRoutePoints().stream().map(RoutePointMapper::createRoutePointResponse).toList()
                : new ArrayList<>();

        routeResponseContent.setRoute_points(routePoints);
        routeResponseContent.setAdditional_information(route.getAdditionalInformation());

        routeResponse.setContent(routeResponseContent);

        return routeResponse;
    }

    public static Route createRouteEntity(RouteRequest routeRequest) {
        var routeEntity = new Route();

        routeEntity.setStartDate(routeRequest.getStart_date());
        routeEntity.setEndDate(routeRequest.getEnd_date());

        routeEntity.setAccommodationPrice(routeRequest.getAccommodation_price());
        routeEntity.setTransportPrice(routeRequest.getTransport_price());

        routeEntity.setAdditionalInformation(routeRequest.getAdditional_information());

        return routeEntity;
    }
}
