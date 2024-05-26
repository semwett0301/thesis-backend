package com.example.services.TicketsService;

import com.example.model.exceptions.AviasalesException;
import com.example.model.dto.internal.TicketsInternalRequest;
import com.example.model.dto.response.TicketResponse;

import java.util.List;
import java.util.Optional;

public interface TicketsService {
    List<TicketResponse> getTickets(TicketsInternalRequest ticketsInternalRequest) throws AviasalesException;
}
