package com.example.services.TicketsService;

import com.example.model.dto.external.aviasales.AviasalesTicketsResponse;
import com.example.model.exceptions.AviasalesException;
import com.example.model.dto.internal.AviasalesInternalRequest;
import com.example.model.dto.internal.TicketsInternalRequest;
import com.example.model.mappers.TicketMapper;
import com.example.model.dto.response.TicketResponse;
import com.example.utils.AviasalesApi.AviasalesApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AviasalesTicketsService implements TicketsService {
    private final AviasalesApi aviasalesApi;

    private final String logosUrl;

    private final String sourceName;

    private final String source;

    private final DateFormat df = new SimpleDateFormat("HH:mm");

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
            final var aviasalesTickets = tickets.stream()
                    .filter(aviasalesTicket -> aviasalesTicket.getPrice() <= ticketsInternalRequest.getMaxPrice())
                    .toList();

            final var finalTicket = aviasalesTickets.size() > 0 ? aviasalesTickets.get(0) : null;

            final List<TicketResponse> resultList = new ArrayList<>();

            if (finalTicket != null) {
                double firstPrice = finalTicket.getPrice() * Math.random();
                int firstResultPrice = (int) firstPrice;

                final var toTicket = toTicketResponse(firstResultPrice,
                        ticketsInternalRequest.getStartCity(),
                        ticketsInternalRequest.getEndCity(),
                        finalTicket.getOrigin_airport(),
                        finalTicket.getDestination_airport(),
                        finalTicket.getDeparture_at(),
                        new Date(finalTicket.getDeparture_at().getTime() + finalTicket.getDuration() * 60 * 1000),
                        finalTicket);

                final var returnTicket = toTicketResponse(finalTicket.getPrice() - firstResultPrice,
                        ticketsInternalRequest.getEndCity(),
                        ticketsInternalRequest.getStartCity(),
                        finalTicket.getDestination_airport(),
                        finalTicket.getOrigin_airport(),
                        finalTicket.getReturn_at(),
                        new Date(finalTicket.getReturn_at().getTime() + finalTicket.getDuration_back() * 60 * 1000),
                        finalTicket);

                resultList.add(toTicket);
                resultList.add(returnTicket);
            }

            return resultList;
        } else {
            throw new AviasalesException();
        }
    }

    private TicketResponse toTicketResponse(Integer price,
                                            String startCity,
                                            String endCity,
                                            String originAirport,
                                            String departureAirport,
                                            Date departureDate,
                                            Date returnDate,
                                            AviasalesTicketsResponse.ResponseData aviasalesTicketsResponse) {
        var companyName = aviasalesTicketsResponse.getAirline();
        var subLink = aviasalesTicketsResponse.getLink();

        var logoUrl = MessageFormat.format("{0}{1}.png", logosUrl, companyName);

        var link = MessageFormat.format("{0}{1}", source, subLink);

        return new TicketResponse(
                logoUrl,
                price,
                sourceName,
                startCity,
                endCity,
                df.format(departureDate),
                df.format(returnDate),
                originAirport,
                departureAirport,
                link
        );
    }
}
