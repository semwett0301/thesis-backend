package com.example.api;

import com.example.model.dto.internal.TicketsInternalRequest;
import com.example.model.dto.request.TicketEnum;
import com.example.model.dto.response.TicketResponse;
import com.example.model.exceptions.AviasalesException;
import com.example.services.TicketsService.AviasalesTicketsService;
import com.example.services.TicketsService.YandexTicketsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TicketsControllerTest {

    @Mock
    private AviasalesTicketsService aviasalesTicketsService;

    @Mock
    private YandexTicketsService yandexTicketsService;

    @InjectMocks
    private TicketsController ticketsController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTickets_Airplane() throws AviasalesException {
        TicketResponse response1 = new TicketResponse("logoUrl1", 100, "source1", "startCity1", "endCity1", "departureTime1", "arrivalTime1", "origin1", "destination1", "link1");
        TicketResponse response2 = new TicketResponse("logoUrl2", 200, "source2", "startCity2", "endCity2", "departureTime2", "arrivalTime2", "origin2", "destination2", "link2");
        List<TicketResponse> responses = Arrays.asList(response1, response2);

        when(aviasalesTicketsService.getTickets(any(TicketsInternalRequest.class))).thenReturn(responses);

        List<TicketResponse> result = ticketsController.getTickets(
                TicketEnum.AIRPLANE,
                new Date(),
                new Date(),
                500,
                "startCity",
                "startCityIata",
                "endCity",
                "endCityIata"
        );

        assertEquals(2, result.size());
        assertEquals("logoUrl1", result.get(0).getLogo_url());
    }

    @Test
    public void testGetTickets_Train() throws AviasalesException {
        TicketResponse response1 = new TicketResponse("logoUrl1", 100, "source1", "startCity1", "endCity1", "departureTime1", "arrivalTime1", "origin1", "destination1", "link1");
        TicketResponse response2 = new TicketResponse("logoUrl2", 200, "source2", "startCity2", "endCity2", "departureTime2", "arrivalTime2", "origin2", "destination2", "link2");
        List<TicketResponse> responses = Arrays.asList(response1, response2);

        when(yandexTicketsService.getTickets(any(TicketsInternalRequest.class))).thenReturn(responses);

        List<TicketResponse> result = ticketsController.getTickets(
                TicketEnum.RAILWAY,
                new Date(),
                new Date(),
                500,
                "startCity",
                "startCityIata",
                "endCity",
                "endCityIata"
        );

        assertEquals(2, result.size());
        assertEquals("logoUrl1", result.get(0).getLogo_url());
    }
}
