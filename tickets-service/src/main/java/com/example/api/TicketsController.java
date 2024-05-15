package com.example.api;

import com.example.model.dto.request.CityRequest;
import com.example.model.dto.request.TicketType;
import com.example.model.dto.response.TicketResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class TicketsController {
    @GetMapping
    public List<TicketResponse> getTickets(
            @RequestParam(name = "type") TicketType ticketType,
            @RequestParam(name = "start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(name = "end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(name = "max_price") Integer maxPrice,
            @RequestParam(name = "max_price") CityRequest city

    ) {
        return null;
    }
}
