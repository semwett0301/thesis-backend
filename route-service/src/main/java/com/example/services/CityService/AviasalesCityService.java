package com.example.services.CityService;

import com.example.model.entities.db.City;
import com.example.repositories.db.CityRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AviasalesCityService implements CityService {
    private CityRepository cityRepository;

    @Override
    public Page<City> searchCity(String search, Integer page, Integer pageSize) {
        var pageable = PageRequest.of(page, pageSize, Sort.by("name"));

        return cityRepository.findByNameContainingIgnoreCase(search, pageable);
    }
}
