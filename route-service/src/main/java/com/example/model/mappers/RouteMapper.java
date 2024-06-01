package com.example.model.mappers;

import com.example.model.dto.request.RouteRequest;
import com.example.model.dto.response.RoutePointResponse;
import com.example.model.dto.response.RouteResponse;
import com.example.model.entities.db.Route;
import com.example.model.utils.RouteStatus;

import java.util.List;
import java.util.UUID;

public class RouteMapper {
    public static RouteResponse createGeneratedRouteResponse(UUID id, RouteRequest routeRequest, List<RoutePointResponse> routePointResponses) {
        var routePointResponse = new RouteResponse();

        routePointResponse.setId(id);
        routePointResponse.setRoute_points(routePointResponses);

        routePointResponse.setAccommodation_price(routeRequest.getAccommodation_price());
        routePointResponse.setTransport_price(routeRequest.getAccommodation_price());
        routePointResponse.setStart_date(routeRequest.getStart_date());
        routePointResponse.setEnd_date(routeRequest.getEnd_date());
        routePointResponse.setAdditional_information(routeRequest.getAdditional_information());
        routePointResponse.setStatus(RouteStatus.GENERATED);

        routePointResponse.setStart_city(routeRequest.getStart_city());
        routePointResponse.setEnd_city(routeRequest.getEnd_city());

        return routePointResponse;
    }

    public static RouteResponse createRouteResponse(Route route) {
        var routeResponse = new RouteResponse();

        routeResponse.setId(route.getId());

        routeResponse.setStart_date(route.getStartDate());
        routeResponse.setEnd_date(route.getEndDate());

        var startCity = CityMapper.INSTANCE.toCityDto(route.getStartCity());
        var endCity = CityMapper.INSTANCE.toCityDto(route.getEndCity());

        routeResponse.setStart_city(startCity);
        routeResponse.setEnd_city(endCity);

        routeResponse.setTransport_price(route.getTransportPrice());
        routeResponse.setAccommodation_price(route.getAccommodationPrice());

        var routePoints = route.getRoutePoints().stream()
                .map(RoutePointMapper::createRoutePointResponse)
                .toList();

        routeResponse.setRoute_points(routePoints);

        routeResponse.setStatus(route.getStatus());
        routeResponse.setAdditional_information(route.getAdditionalInformation());

        return routeResponse;
    }

    public static Route createRouteEntity(RouteRequest routeRequest) {
        var routeEntity = new Route();

        routeEntity.setStartDate(routeRequest.getStart_date());
        routeEntity.setEndDate(routeRequest.getEnd_date());

        routeEntity.setAccommodationPrice(routeRequest.getAccommodation_price());
        routeEntity.setTransportPrice(routeRequest.getTransport_price());

        routeEntity.setStatus(RouteStatus.GENERATED);

        routeEntity.setAdditionalInformation(routeRequest.getAdditional_information());

        return routeEntity;
    }
}
