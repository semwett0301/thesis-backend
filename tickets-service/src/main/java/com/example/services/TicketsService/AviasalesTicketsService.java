package com.example.services.TicketsService;

import com.example.model.dto.exceptions.AviasalesException;
import com.example.model.dto.internal.AviasalesInternalRequest;
import com.example.model.dto.internal.TicketsInternalRequest;
import com.example.model.dto.response.TicketResponse;
import com.example.utils.AviasalesApi.AviasalesApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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

        var df = new SimpleDateFormat("HH:mm");

        if (success) {
            return tickets.stream()
                    .filter(aviasalesTicket -> aviasalesTicket.getPrice() <= ticketsInternalRequest.getMaxPrice() / 2 + 1)
                    .map(aviasalesTicket -> {
                        var companyName = aviasalesTicket.getAirline();
                        var subLink = aviasalesTicket.getLink();

                        var logoUrl = """
                                {logosUrl}{companyName}.png
                                """;

                        var link = """
                                {source}{link}
                                """;

                        return new TicketResponse(
                                logoUrl,
                                aviasalesTicket.getPrice(),
                                sourceName,
                                ticketsInternalRequest.getStartCity(),
                                ticketsInternalRequest.getEndCity(),
                                df.format(aviasalesTicket.getDepartureAt()),
                                df.format(aviasalesTicket.getReturnAt()),
                                aviasalesTicket.getOrigin(),
                                aviasalesTicket.getDestinationAirport(),
                                link
                        );
                    })
                    .collect(Collectors.toList());
        } else {
            throw new AviasalesException();
        }
    }
}
