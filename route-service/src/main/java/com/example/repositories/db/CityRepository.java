package com.example.repositories.db;

import com.example.model.entities.db.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, String> {
    Page<City> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
