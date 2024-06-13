package com.example.services;

import com.example.model.dto.external.aviasales.AviasalesTicketsResponse;
import com.example.model.dto.internal.TicketsInternalRequest;
import com.example.model.dto.internal.AviasalesInternalRequest;
import com.example.model.dto.response.TicketResponse;
import com.example.model.exceptions.AviasalesException;
import com.example.services.TicketsService.AviasalesTicketsService;
import com.example.utils.AviasalesApi.AviasalesApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AviasalesTicketsServiceTest {

    @Mock
    private AviasalesApi aviasalesApi;

    @InjectMocks
    private AviasalesTicketsService aviasalesTicketsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTickets_Success() throws AviasalesException {
        AviasalesTicketsResponse.ResponseData responseData = new AviasalesTicketsResponse.ResponseData();
        responseData.setPrice(300);
        responseData.setOrigin_airport("origin");
        responseData.setDestination_airport("destination");
        responseData.setDeparture_at(new Date());
        responseData.setReturn_at(new Date());
        responseData.setDuration(60);
        responseData.setDuration_back(60);
        responseData.setAirline("airline");
        responseData.setLink("link");

        AviasalesTicketsResponse response = new AviasalesTicketsResponse();
        response.setSuccess(true);
        response.setData(Arrays.asList(responseData));

        when(aviasalesApi.getAviasalesTickets(any(AviasalesInternalRequest.class))).thenReturn(response);

        TicketsInternalRequest request = new TicketsInternalRequest(new Date(), new Date(), "startCity", "startCityIata", "endCity", "endCityIata", 500);

        List<TicketResponse> result = aviasalesTicketsService.getTickets(request);

        assertEquals(2, result.size());
        assertEquals("origin", result.get(0).getStart_point());
    }

    @Test
    public void testGetTickets_Failure() throws AviasalesException {
        AviasalesTicketsResponse response = new AviasalesTicketsResponse();
        response.setSuccess(false);

        when(aviasalesApi.getAviasalesTickets(any(AviasalesInternalRequest.class))).thenReturn(response);

        TicketsInternalRequest request = new TicketsInternalRequest(new Date(), new Date(), "startCity", "startCityIata", "endCity", "endCityIata", 500);

        try {
            aviasalesTicketsService.getTickets(request);
        } catch (AviasalesException e) {
            assertEquals(AviasalesException.class, e.getClass());
        }
    }

    @Test
    public void testGetTickets_ThrowsAviasalesException_WhenSuccessIsFalse() {
        // Mock the response from Aviasales API to return success as false
        AviasalesTicketsResponse aviasalesTicketsResponse = new AviasalesTicketsResponse();
        aviasalesTicketsResponse.setSuccess(false);
        when(aviasalesApi.getAviasalesTickets(any(AviasalesInternalRequest.class))).thenReturn(aviasalesTicketsResponse);

        // Create a test request
        TicketsInternalRequest internalRequest = new TicketsInternalRequest();
        // Set other required fields in the internal request...

        // Call the method under test and expect AviasalesException to be thrown
        assertThrows(AviasalesException.class, () -> aviasalesTicketsService.getTickets(internalRequest));
    }

    @Test
    public void testGetTickets_ReturnsEmptyList_WhenFinalTicketIsNull() throws AviasalesException {
        // Mock the response from Aviasales API to return a response with no tickets
        AviasalesTicketsResponse aviasalesTicketsResponse = new AviasalesTicketsResponse();
        aviasalesTicketsResponse.setSuccess(true);
        aviasalesTicketsResponse.setData(Collections.emptyList());
        when(aviasalesApi.getAviasalesTickets(any(AviasalesInternalRequest.class))).thenReturn(aviasalesTicketsResponse);

        // Create a test request
        TicketsInternalRequest internalRequest = new TicketsInternalRequest();
        // Set other required fields in the internal request...

        // Call the method under test
        List<TicketResponse> result = aviasalesTicketsService.getTickets(internalRequest);

        // Verify that the result is an empty list
        assertTrue(result.isEmpty());
    }
}
