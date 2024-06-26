package com.example.repositories.db;

import com.example.model.entities.db.Route;
import com.example.model.entities.db.UserInfo;
import com.example.model.utils.RouteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface RouteRepository extends JpaRepository<Route, UUID> {
    List<Route> findByStartDateAfterAndUser(Date date, UserInfo user);
    List<Route> findByStatusNotOrStartDateBefore(RouteStatus status, Date date);
}
