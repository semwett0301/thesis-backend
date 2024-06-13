package com.example.utils;

import com.example.model.dto.external.yandex.YandexTicketsResponse;
import com.example.model.dto.internal.YandexInternalRequest;
import com.example.utils.YandexApi.YandexApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class YandexApiTest {

    @MockBean
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private YandexApi yandexApi;

    private final String uri = "https://api.rasp.yandex.net/v3.0";
    private final String token = "68accdce-21bf-4120-94c1-0600c3ebdc60";

    @BeforeEach
    public void setUp() {
        when(webClientBuilder.baseUrl(any(String.class))).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        yandexApi = new YandexApi(uri, token);
    }

    @Test
    public void testGetYandexTickets() {
        YandexTicketsResponse mockResponse = new YandexTicketsResponse();
        mockResponse.setSearch(new YandexTicketsResponse.Search());
        mockResponse.setSegments(null);
        mockResponse.setIntervalSegments(null);
        mockResponse.setPagination(null);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(YandexTicketsResponse.class)).thenReturn(Mono.just(mockResponse));

        YandexInternalRequest request = new YandexInternalRequest(
                "MOW",
                "LED",
                new Date()
        );

        YandexTicketsResponse response = yandexApi.getYandexTickets(request);
    }
}
