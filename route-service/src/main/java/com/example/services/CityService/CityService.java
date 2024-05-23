package com.example.services.CityService;

import com.example.model.entities.db.City;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;

public interface CityService {
    public Page<City> searchCity(String search, @Min(1) Integer page, @Min(1) Integer pageSize);
}
