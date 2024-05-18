package com.example.api;

import com.example.CityDto;
import com.example.model.dto.response.AccommodationResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class AccommodationsController {
    @GetMapping
    public List<AccommodationResponse> getAccommodations(
            @RequestParam(name = "start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(name = "end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(name = "max_price") Integer maxPrice,
            @RequestParam(name = "city") CityDto city
    ) {
        return null;
    }
}
