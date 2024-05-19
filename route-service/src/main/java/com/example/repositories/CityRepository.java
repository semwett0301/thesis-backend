package com.example.repositories;

import com.example.model.entities.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, String> {
    Page<City> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
