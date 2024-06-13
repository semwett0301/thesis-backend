package com.example.services;

import com.example.model.entities.db.City;
import com.example.repositories.db.CityRepository;
import com.example.services.CityService.AviasalesCityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class AviasalesCityServiceTest {

    @Autowired
    private AviasalesCityService aviasalesCityService;

    @Autowired
    private CityRepository cityRepository;

    @BeforeEach
    void setUp() {
        cityRepository.deleteAll();

        City city1 = new City();
        city1.setIata("NYC");
        city1.setName("New York");

        City city2 = new City();
        city2.setIata("EWR");
        city2.setName("Newark");

        City city3 = new City();
        city3.setIata("NEW");
        city3.setName("New Orleans");

        City city4 = new City();
        city4.setIata("NEO");
        city4.setName("Newport");

        cityRepository.saveAll(Arrays.asList(city1, city2, city3, city4));
    }

    @Test
    void searchCity() {
        // Arrange
        String search = "New";
        int page = 0;
        int pageSize = 10;
        PageRequest pageable = PageRequest.of(page, pageSize, Sort.by("name"));

        Page<City> result = aviasalesCityService.searchCity(search, page, pageSize);

        assertEquals(4, result.getTotalElements());
        assertEquals("New Orleans", result.getContent().get(0).getName());
        assertEquals("New York", result.getContent().get(1).getName());
        assertEquals("Newark", result.getContent().get(2).getName());
        assertEquals("Newport", result.getContent().get(3).getName());
    }

    @Test
    void searchCityNotFound() {
        String search = "NonExistentCity";
        int page = 0;
        int pageSize = 10;
        PageRequest pageable = PageRequest.of(page, pageSize, Sort.by("name"));

        Page<City> result = aviasalesCityService.searchCity(search, page, pageSize);

        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getContent().size());
    }
}
