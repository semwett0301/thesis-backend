package com.example.api;

import com.example.City;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cities")
public class CitiesController {
    @GetMapping
    public List<City> getCities(
            @RequestParam(name = "search") String search,
            @RequestParam(name = "page") @Min(1) Integer page,
            @RequestParam(name = "page_size") @Min(1) String pageSize
    ) {
        return null;
    }
}
