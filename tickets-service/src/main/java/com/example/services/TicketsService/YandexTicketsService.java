package com.example.services.TicketsService;

import com.example.model.dto.internal.TicketsInternalRequest;
import com.example.model.dto.response.TicketResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class YandexTicketsService implements TicketsService{
    @Override
    public List<TicketResponse> getTickets(TicketsInternalRequest ticketsInternalRequest) {
        return null;
    }
}
