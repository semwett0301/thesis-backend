package com.example.utils.YandexApi;

import com.example.model.dto.external.yandex.YandexTicketsResponse;
import com.example.model.dto.internal.YandexInternalRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.SimpleDateFormat;

@Component
public class YandexApi {
    private final WebClient webClient;

    private final String token;


    public YandexApi(@Value("${yandex.uri}") String uri, @Value("${yandex.token}") String token) {
        this.webClient = WebClient.builder()
                .baseUrl(uri)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.token = token;
    }

    public YandexTicketsResponse getYandexTickets(YandexInternalRequest yandexInternalRequest) {
        var df = new SimpleDateFormat("yyyy-MM-dd");

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/")
                        .queryParam("apikey", token)
                        .queryParam("from", yandexInternalRequest.getFrom())
                        .queryParam("to", yandexInternalRequest.getTo())
                        .queryParam("date", df.format(yandexInternalRequest.getDate()))
                        .queryParam("limit", 1)
                        .queryParam("transport_types", "train")
                        .queryParam("system", "iata")
                        .build())
                .retrieve()
                .bodyToMono(YandexTicketsResponse.class)
                .block();
    }
}
