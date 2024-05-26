package com.example.utils.AviasalesApi;

import com.example.model.dto.external.aviasales.AviasalesTicketsResponse;
import com.example.model.dto.internal.AviasalesInternalRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.SimpleDateFormat;

@Component
public class AviasalesApi {
    private final WebClient webClient;

    private final String token;


    public AviasalesApi(@Value("${aviasales.uri}") String uri, @Value("${aviasales.token}") String token) {
        this.webClient = WebClient.builder()
                .baseUrl(uri)
                .build();
        this.token = token;
    }

    public AviasalesTicketsResponse getAviasalesTickets(AviasalesInternalRequest aviasalesInternalRequest) {
        var df = new SimpleDateFormat("yyyy-MM-dd");

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/prices_for_dates")
                        .queryParam("origin", aviasalesInternalRequest.getOrigin())
                        .queryParam("destination", aviasalesInternalRequest.getDestination())
                        .queryParam("departure_at ", df.format(aviasalesInternalRequest.getDepartureAt()))
                        .queryParam("return_at ", df.format(aviasalesInternalRequest.getReturnAt()))
                        .queryParam("one_way", aviasalesInternalRequest.getOneWay())
                        .queryParam("token", token)
                        .queryParam("limit", aviasalesInternalRequest.getLimit())
                        .queryParam("page", aviasalesInternalRequest.getPage())
                        .build())
                .retrieve()
                .bodyToMono(AviasalesTicketsResponse.class)
                .block();
    }
}
