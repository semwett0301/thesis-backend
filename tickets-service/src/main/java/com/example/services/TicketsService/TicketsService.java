package com.example.services.TicketsService;

import com.example.model.dto.exceptions.AviasalesException;
import com.example.model.dto.internal.TicketsInternalRequest;
import com.example.model.dto.response.TicketResponse;

import java.util.Date;
import java.util.List;

public interface TicketsService {
    List<TicketResponse> getTickets(TicketsInternalRequest ticketsInternalRequest) throws AviasalesException;
}
