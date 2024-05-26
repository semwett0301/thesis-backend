package com.example.api;

import com.example.model.exceptions.AviasalesException;
import com.example.model.dto.internal.TicketsInternalRequest;
import com.example.model.dto.request.TicketEnum;
import com.example.model.dto.response.TicketResponse;
import com.example.services.TicketsService.AviasalesTicketsService;
import com.example.services.TicketsService.YandexTicketsService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/tickets")
@AllArgsConstructor
public class TicketsController {
    private AviasalesTicketsService aviasalesTicketsService;
    private YandexTicketsService yandexTicketsService;

    @GetMapping
    public List<TicketResponse> getTickets(
            @RequestParam(name = "type") TicketEnum ticketType,
            @RequestParam(name = "start_date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "end_date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(name = "max_price") Integer maxPrice,
            @RequestParam(name = "start_city") String startCity,
            @RequestParam(name = "start_city_iata") String startCityIata,
            @RequestParam(name = "end_city") String endCity,
            @RequestParam(name = "end_city_iata") String endCityIata
    ) throws AviasalesException {
        var ticketsRequest = new TicketsInternalRequest(startDate, endDate, startCity, startCityIata, endCity, endCityIata, maxPrice);

        return ticketType == TicketEnum.AIRPLANE ?
                aviasalesTicketsService.getTickets(ticketsRequest) :
                yandexTicketsService.getTickets(ticketsRequest);
    }
}
