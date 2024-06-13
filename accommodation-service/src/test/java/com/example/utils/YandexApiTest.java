package com.example.utils;

import com.example.model.dto.external.YandexAccommodationResponse;
import com.example.model.dto.external.YandexSuggestionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class YandexApiTest {

    @Mock
    private WebClient webClientSuggestion;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecSuggestion;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpecSuggestion;

    @Mock
    private WebClient.ResponseSpec responseSpecSuggestion;

    @Mock
    private WebClient webClientResult;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecResult;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpecResult;

    @Mock
    private WebClient.ResponseSpec responseSpecResult;

    @InjectMocks
    private YandexApi yandexApi;

    @Value("${yandex.token}")
    private String token;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        yandexApi = new YandexApi("http://test-uri", "http://test-suggestion-uri", token);
        yandexApi.setWebClientResult(webClientResult);
        yandexApi.setWebClientSuggestion(webClientSuggestion);
    }

    @Test
    void getYandexSuggestion_Success() {
        YandexSuggestionResponse response = new YandexSuggestionResponse();
        YandexSuggestionResponse.Results results = new YandexSuggestionResponse.Results();
        YandexSuggestionResponse.Location location = new YandexSuggestionResponse.Location();
        location.setId("1");
        results.setLocations(Collections.singletonList(location));
        response.setResults(results);

        when(webClientSuggestion.get()).thenReturn(requestHeadersUriSpecSuggestion);
        when(requestHeadersUriSpecSuggestion.uri(anyString(), (Object) any())).thenReturn(requestHeadersSpecSuggestion);
        when(requestHeadersSpecSuggestion.retrieve()).thenReturn(responseSpecSuggestion);
        when(responseSpecSuggestion.bodyToMono(YandexSuggestionResponse.class)).thenReturn(Mono.just(response));

        YandexSuggestionResponse result = yandexApi.getYandexSuggestion("XYZ");

        assertNotNull(result);
        assertEquals("1", result.getResults().getLocations().get(0).getId());
    }

    @Test
    void getYandexSuggestion_Failure() {
        when(webClientSuggestion.get()).thenReturn(requestHeadersUriSpecSuggestion);
        when(requestHeadersUriSpecSuggestion.uri(anyString(), (Object) any())).thenReturn(requestHeadersSpecSuggestion);
        when(requestHeadersSpecSuggestion.retrieve()).thenReturn(responseSpecSuggestion);
        when(responseSpecSuggestion.bodyToMono(YandexSuggestionResponse.class)).thenThrow(WebClientResponseException.class);

        assertThrows(WebClientResponseException.class, () -> yandexApi.getYandexSuggestion("XYZ"));
    }

    @Test
    void getYandexTickets_Success() {
        YandexAccommodationResponse response = new YandexAccommodationResponse();
        YandexAccommodationResponse.CheapHotel hotel = new YandexAccommodationResponse.CheapHotel();
        hotel.setLast_price_info(new YandexAccommodationResponse.LastPriceInfo());
        hotel.setDistance(1.0);
        hotel.setName("Hotel Name");
        hotel.setRating(90);
        response.setCheaphotel(Collections.singletonList(hotel));

        when(webClientResult.get()).thenReturn(requestHeadersUriSpecResult);
        when(requestHeadersUriSpecResult.uri(anyString(), (Object) any())).thenReturn(requestHeadersSpecResult);
        when(requestHeadersSpecResult.retrieve()).thenReturn(responseSpecResult);
        when(responseSpecResult.bodyToMono(YandexAccommodationResponse.class)).thenReturn(Mono.just(response));

        Date startDate = new Date();
        Date endDate = new Date();
        YandexAccommodationResponse result = yandexApi.getYandexTickets("1", startDate, endDate);

        assertNotNull(result);
        assertEquals(1, result.getCheaphotel().size());
        assertEquals(100, result.getCheaphotel().get(0).getLast_price_info().getPrice());
        assertEquals(1.0, result.getCheaphotel().get(0).getDistance());
        assertEquals("Hotel Name", result.getCheaphotel().get(0).getName());
    }

    @Test
    void getYandexTickets_Failure() {
        when(webClientResult.get()).thenReturn(requestHeadersUriSpecResult);
        when(requestHeadersUriSpecResult.uri(anyString(), (Object) any())).thenReturn(requestHeadersSpecResult);
        when(requestHeadersSpecResult.retrieve()).thenReturn(responseSpecResult);
        when(responseSpecResult.bodyToMono(YandexAccommodationResponse.class)).thenThrow(WebClientResponseException.class);

        Date startDate = new Date();
        Date endDate = new Date();
        assertThrows(WebClientResponseException.class, () -> yandexApi.getYandexTickets("1", startDate, endDate));
    }
}
