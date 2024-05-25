package com.example.repositories.db;

import com.example.model.entities.db.RoutePoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoutePointRepository extends JpaRepository<RoutePoint, UUID> {
}
