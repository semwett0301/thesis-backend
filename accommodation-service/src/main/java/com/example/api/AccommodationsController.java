package com.example.api;

import com.example.model.dto.internal.AccommodationRequest;
import com.example.model.dto.response.AccommodationResponse;
import com.example.services.AccommodationService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/accommodations")
@AllArgsConstructor
public class AccommodationsController {
    private AccommodationService accommodationService;

    @GetMapping
    public List<AccommodationResponse> getAccommodations(
            @RequestParam(name = "start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(name = "end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(name = "max_price") Integer maxPrice,
            @RequestParam(name = "city_iata") String cityIata
    ) {
        return accommodationService.getAccommodations(new AccommodationRequest(startDate, endDate, maxPrice, cityIata));
    }
}
