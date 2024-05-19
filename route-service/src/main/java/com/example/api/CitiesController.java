package com.example.api;

import com.example.model.entities.City;
import com.example.services.CityService.CityService;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cities")
@AllArgsConstructor
@Validated
public class CitiesController {
    private CityService cityService;

    @GetMapping
    public Page<City> getCities(
            @RequestParam(name = "search", required = false, defaultValue = "") String search,
            @RequestParam(name = "page", required = false, defaultValue = "1") @Min(value = 0) Integer page,
            @RequestParam(name = "page_size", required = false, defaultValue = "10") @Min(value = 1) Integer pageSize
    ) {
        return cityService.searchCity(search.trim(), page, pageSize);
    }
}
