package com.example.services.TicketsService;

import com.example.model.dto.exceptions.AviasalesException;
import com.example.model.dto.internal.AviasalesInternalRequest;
import com.example.model.dto.internal.TicketsInternalRequest;
import com.example.model.dto.mappers.TicketMapper;
import com.example.model.dto.response.TicketResponse;
import com.example.utils.AviasalesApi.AviasalesApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AviasalesTicketsService implements TicketsService {
    private final AviasalesApi aviasalesApi;

    private final String logosUrl;

    private final String sourceName;

    private final String source;

    public AviasalesTicketsService(AviasalesApi aviasalesApi,
                                   @Value("${aviasales.logos-uri}") String logosUrl,
                                   @Value("${aviasales.source}") String source,
                                   @Value("${aviasales.source-name}") String sourceName) {
        this.aviasalesApi = aviasalesApi;
        this.logosUrl = logosUrl;
        this.sourceName = sourceName;
        this.source = source;
    }

    @Override
    public List<TicketResponse> getTickets(TicketsInternalRequest ticketsInternalRequest) throws AviasalesException {
        var ticketsParams = new AviasalesInternalRequest(ticketsInternalRequest.getEndCityIata(),
                ticketsInternalRequest.getStartCityIata(),
                ticketsInternalRequest.getStartDate(),
                ticketsInternalRequest.getEndDate(),
                20,
                1,
                false);

        var response = aviasalesApi.getAviasalesTickets(ticketsParams);
        var success = response.getSuccess();
        var tickets = response.getData();


        if (success) {
            return tickets.stream()
                    .filter(aviasalesTicket -> aviasalesTicket.getPrice() <= ticketsInternalRequest.getMaxPrice())
                    .map(aviasalesTicket -> TicketMapper.toTicketResponse(source, logosUrl, sourceName, aviasalesTicket, ticketsInternalRequest))
                    .collect(Collectors.toList());
        } else {
            throw new AviasalesException();
        }
    }
}
