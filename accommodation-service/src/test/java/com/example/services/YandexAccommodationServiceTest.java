package com.example.services;

import com.example.model.dto.external.YandexAccommodationResponse;
import com.example.model.dto.external.YandexSuggestionResponse;
import com.example.model.dto.internal.AccommodationRequest;
import com.example.model.dto.response.AccommodationResponse;
import com.example.utils.YandexApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class YandexAccommodationServiceTest {

    @Mock
    private YandexApi yandexApi;

    @InjectMocks
    private YandexAccommodationService yandexAccommodationService;

    @Value("${yandex.source}")
    private String source;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAccommodations_Success() {
        Date startDate = new Date();
        Date endDate = new Date();
        AccommodationRequest request = new AccommodationRequest(startDate, endDate, 200, "XYZ");

        YandexSuggestionResponse suggestionCity = new YandexSuggestionResponse();
        suggestionCity.setResults(new YandexSuggestionResponse.Results());

        YandexAccommodationResponse.CheapHotel hotel = new YandexAccommodationResponse.CheapHotel();
        hotel.setLast_price_info(new YandexAccommodationResponse.LastPriceInfo());
        hotel.setDistance(1.0);
        hotel.setName("Hotel Name");
        hotel.setRating(90);
        YandexAccommodationResponse hotelsResponse = new YandexAccommodationResponse();
        hotelsResponse.setCheaphotel(Arrays.asList(hotel));

        when(yandexApi.getYandexSuggestion(anyString())).thenReturn(suggestionCity);
        when(yandexApi.getYandexTickets(anyString(), any(Date.class), any(Date.class))).thenReturn(hotelsResponse);

        List<AccommodationResponse> accommodations = yandexAccommodationService.getAccommodations(request);

        assertNotNull(accommodations);
        assertEquals(1, accommodations.size());
        assertEquals(100, accommodations.get(0).getPrice());
        assertEquals(1.0, accommodations.get(0).getCenter_distance());
        assertEquals("Hotel Name", accommodations.get(0).getName());
        assertEquals("source", accommodations.get(0).getSource());
        assertEquals(4.5, accommodations.get(0).getRating());
        assertEquals(90, accommodations.get(0).getReviews_amount());
    }

    @Test
    void getAccommodations_InvalidIataCode() {
        Date startDate = new Date();
        Date endDate = new Date();
        AccommodationRequest request = new AccommodationRequest(startDate, endDate, 200, "XYZ");

        YandexSuggestionResponse suggestionCity = new YandexSuggestionResponse();
        suggestionCity.setResults(new YandexSuggestionResponse.Results());

        when(yandexApi.getYandexSuggestion(anyString())).thenReturn(suggestionCity);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> yandexAccommodationService.getAccommodations(request));
        assertEquals("Wrong iata code", exception.getReason());
    }
}
