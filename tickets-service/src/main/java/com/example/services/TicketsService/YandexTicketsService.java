package com.example.services.TicketsService;

import com.example.model.dto.external.yandex.YandexTicketsResponse;
import com.example.model.dto.internal.TicketsInternalRequest;
import com.example.model.dto.internal.YandexInternalRequest;
import com.example.model.dto.response.TicketResponse;
import com.example.utils.YandexApi.YandexApi;
import io.netty.util.internal.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class YandexTicketsService implements TicketsService {
    private final YandexApi yandexApi;

    private final String source;

    private final String logoUrl;

    private final String baseLink;


    public YandexTicketsService(YandexApi yandexApi, @Value("${yandex.source}") String source, @Value("${yandex.logo-url}") String logoUrl, @Value("${yandex.base-link}") String baseLink) {
        this.yandexApi = yandexApi;
        this.source = source;
        this.logoUrl = logoUrl;
        this.baseLink = baseLink;
    }

    @Override
    public List<TicketResponse> getTickets(TicketsInternalRequest ticketsInternalRequest) {
        var ticketToRequest = new YandexInternalRequest(ticketsInternalRequest.getStartCityIata(), ticketsInternalRequest.getEndCityIata(), ticketsInternalRequest.getStartDate());
        var ticketFromRequest = new YandexInternalRequest(ticketsInternalRequest.getEndCityIata(), ticketsInternalRequest.getStartCityIata(), ticketsInternalRequest.getEndDate());

        var ticketTo = yandexApi.getYandexTickets(ticketToRequest);
        var ticketFrom = yandexApi.getYandexTickets(ticketFromRequest);

        List<TicketResponse> result = new ArrayList<>();

        if (ticketFrom.getSegments().size() > 0 && ticketTo.getSegments().size() > 0) {
            var firstSegment = ticketTo.getSegments().get(0);
            var secondSegment = ticketFrom.getSegments().get(0);

            var firstPrice = 0.2483 * ticketsInternalRequest.getMaxPrice();
            var secondPrice = 0.3574 * ticketsInternalRequest.getMaxPrice();

            var firstLink = MessageFormat.format("{0}/{1}", baseLink, firstSegment.getThread().getUid());
            var secondLink = MessageFormat.format("{0}/{1}", baseLink, firstSegment.getThread().getUid());

            result.add(getTicketResponseFromSegment(ticketsInternalRequest.getStartCity(), ticketsInternalRequest.getEndCity(), firstSegment, (int) Math.floor(firstPrice), firstLink));
            result.add(getTicketResponseFromSegment(ticketsInternalRequest.getEndCity(), ticketsInternalRequest.getStartCity(), secondSegment, (int) Math.floor(secondPrice), secondLink));
        }

        return result;
    }

    private TicketResponse getTicketResponseFromSegment(String startCity, String endCity, YandexTicketsResponse.Segment segment, Integer price, String link) {
        var df = new SimpleDateFormat("HH:mm");

        return new TicketResponse(logoUrl,
                price,
                source,
                startCity,
                endCity,
                df.format(segment.getDeparture()),
                df.format(segment.getArrival()),
                segment.getFrom().getShort_title(),
                segment.getTo().getShort_title(),
                link);
    }
}
