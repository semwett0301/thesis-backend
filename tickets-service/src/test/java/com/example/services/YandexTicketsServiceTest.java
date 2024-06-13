package com.example.services.TicketsService;

import com.example.model.dto.external.yandex.YandexTicketsResponse;
import com.example.model.dto.internal.TicketsInternalRequest;
import com.example.model.dto.internal.YandexInternalRequest;
import com.example.model.dto.response.TicketResponse;
import com.example.utils.YandexApi.YandexApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class YandexTicketsServiceTest {

    @Mock
    private YandexApi yandexApi;

    @InjectMocks
    private YandexTicketsService yandexTicketsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTickets() {
        YandexTicketsResponse.Segment segment1 = new YandexTicketsResponse.Segment();
        segment1.setThread(new YandexTicketsResponse.ThreadInfo());
        segment1.getThread().setUid("uid1");
        segment1.setDeparture(new Date());
        segment1.setArrival(new Date());
        segment1.setFrom(new YandexTicketsResponse.Station());
        segment1.getFrom().setShort_title("origin1");
        segment1.setTo(new YandexTicketsResponse.Station());
        segment1.getTo().setShort_title("destination1");

        YandexTicketsResponse response1 = new YandexTicketsResponse();
        response1.setSegments(List.of(segment1));

        when(yandexApi.getYandexTickets(any(YandexInternalRequest.class))).thenReturn(response1);

        TicketsInternalRequest request = new TicketsInternalRequest(new Date(), new Date(), "startCity", "startCityIata", "endCity", "endCityIata", 500);

        List<TicketResponse> result = yandexTicketsService.getTickets(request);

        assertEquals(2, result.size());
        assertEquals("origin1", result.get(0).getStart_point());
        assertEquals("destination1", result.get(0).getEnd_point());
    }

    @Test
    public void testGetTickets_ReturnsEmptyList_WhenNoSegments() {
        // Mock the response from Yandex API to return a response with no segments
        YandexTicketsResponse yandexTicketsResponse = new YandexTicketsResponse();
        yandexTicketsResponse.setSegments(Collections.emptyList());
        when(yandexApi.getYandexTickets(any(YandexInternalRequest.class))).thenReturn(yandexTicketsResponse);

        // Create a test request
        TicketsInternalRequest internalRequest = new TicketsInternalRequest();
        // Set other required fields in the internal request...

        // Call the method under test
        List<TicketResponse> result = yandexTicketsService.getTickets(internalRequest);

        // Verify that the result is an empty list
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTickets_ReturnsEmptyList_WhenNoSegmentsInTicketTo() {
        // Mock the response from Yandex API to return a response with no segments for ticketTo
        YandexTicketsResponse yandexTicketsResponse = new YandexTicketsResponse();
        yandexTicketsResponse.setSegments(Collections.emptyList());
        when(yandexApi.getYandexTickets(any(YandexInternalRequest.class))).thenReturn(yandexTicketsResponse);

        // Create a test request
        TicketsInternalRequest internalRequest = new TicketsInternalRequest();
        // Set other required fields in the internal request...

        // Call the method under test
        List<TicketResponse> result = yandexTicketsService.getTickets(internalRequest);

        // Verify that the result is an empty list
        assertTrue(result.isEmpty());
    }
}
