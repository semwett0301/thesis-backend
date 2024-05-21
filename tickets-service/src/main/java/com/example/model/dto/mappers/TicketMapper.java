package com.example.model.dto.mappers;

import com.example.model.dto.external.aviasales.AviasalesTicketsResponse;
import com.example.model.dto.internal.TicketsInternalRequest;
import com.example.model.dto.response.TicketResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TicketMapper {
    private static DateFormat df = new SimpleDateFormat("HH:mm");


    public static TicketResponse toTicketResponse(String source,
                                                  String logosUrl,
                                                  String sourceName,
                                                  AviasalesTicketsResponse.ResponseData aviasalesTicket,
                                                  TicketsInternalRequest ticketsInternalRequest) {
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
    }
}
