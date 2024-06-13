package com.example.utils;

import com.example.model.dto.external.YandexAccommodationResponse;
import com.example.model.dto.external.YandexSuggestionResponse;
import com.example.model.dto.internal.AccommodationRequest;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Setter
public class YandexApi {
    private WebClient webClientSuggestion;

    private WebClient webClientResult;

    private final String token;

    public YandexApi(@Value("${yandex.uri}") String uri, @Value("${yandex.suggest-uri}") String suggestionUri, @Value("${yandex.token}") String token) {
        this.webClientResult = WebClient.builder()
                .baseUrl(uri)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();

        this.webClientSuggestion = WebClient.builder()
                .baseUrl(suggestionUri)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();

        this.token = token;
    }

    public YandexSuggestionResponse getYandexSuggestion(String iata) {
        return webClientSuggestion.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/lookup.json")
                        .queryParam("token", token)
                        .queryParam("query", iata)
                        .queryParam("language", "ru")
                        .queryParam("lookFor", "city")
                        .queryParam("limit", 1)
                        .build())
                .retrieve()
                .bodyToMono(YandexSuggestionResponse.class)
                .block();
    }

    public YandexAccommodationResponse getYandexTickets(String id, Date startDate, Date endDate) {
        var df = new SimpleDateFormat("yyyy-MM-dd");

        return webClientResult.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/widget_location_dump.json")
                        .queryParam("token", token)
                        .queryParam("id", id)
                        .queryParam("currency", "rub")
                        .queryParam("language", "ru")
                        .queryParam("type", "cheaphotel")
                        .queryParam("limit", 100)
                        .queryParam("check_in", df.format(startDate))
                        .queryParam("check_out", df.format(endDate))
                        .build())
                .retrieve()
                .bodyToMono(YandexAccommodationResponse.class)
                .block();
    }
}
