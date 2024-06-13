package com.example.utils;

import com.example.model.dto.external.aviasales.AviasalesTicketsResponse;
import com.example.model.dto.internal.AviasalesInternalRequest;
import com.example.utils.AviasalesApi.AviasalesApi;
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
public class AviasalesApiTest {

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
    private AviasalesApi aviasalesApi;

    private final String uri = "https://api.travelpayouts.com/aviasales/v3";
    private final String token = "129691c2d364db5f8aea5b915173cad6";

    @BeforeEach
    public void setUp() {
        when(webClientBuilder.baseUrl(any(String.class))).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        aviasalesApi = new AviasalesApi(uri, token);
    }

    @Test
    public void testGetAviasalesTickets() {
        AviasalesTicketsResponse mockResponse = new AviasalesTicketsResponse();
        mockResponse.setSuccess(true);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(AviasalesTicketsResponse.class)).thenReturn(Mono.just(mockResponse));

        AviasalesInternalRequest request = new AviasalesInternalRequest(
                "MOW",
                "LED",
                new Date(),
                new Date(),
                20,
                1,
                false
        );

        AviasalesTicketsResponse response = aviasalesApi.getAviasalesTickets(request);

        assertEquals(true, response.getSuccess());
    }
}
