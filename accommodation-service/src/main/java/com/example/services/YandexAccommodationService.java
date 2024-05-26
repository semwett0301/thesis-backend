package com.example.services;

import com.example.model.dto.internal.AccommodationRequest;
import com.example.model.dto.response.AccommodationResponse;
import com.example.utils.YandexApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class YandexAccommodationService implements AccommodationService {
    private final YandexApi yandexApi;

    private final String source;

    public YandexAccommodationService(YandexApi yandexApi, @Value("${yandex.source}") String source) {
        this.yandexApi = yandexApi;
        this.source = source;
    }

    @Override
    public List<AccommodationResponse> getAccommodations(AccommodationRequest accommodationRequest) {
        var suggestionCity = yandexApi.getYandexSuggestion(accommodationRequest.getCityIata());

        if (suggestionCity.getResults().getLocations().size() > 0) {
            var cityId = suggestionCity.getResults().getLocations().get(0).getId();

            var hotelsResponse = yandexApi.getYandexTickets(cityId, accommodationRequest.getStartDate(), accommodationRequest.getEndDate());

            return hotelsResponse.getCheaphotel().stream()
                    .filter(hotel -> hotel.getLast_price_info() != null && hotel.getLast_price_info().getPrice() < accommodationRequest.getMaxPrice())
                    .map(hotel -> new AccommodationResponse(hotel.getLast_price_info().getPrice(), hotel.getDistance(), hotel.getName(), source, (double) (5 * hotel.getRating() / 100), hotel.getRating()))
                    .limit(10)
                    .toList();
        }

        throw new ResponseStatusException(BAD_REQUEST, "Wrong iata code");
    }
}
