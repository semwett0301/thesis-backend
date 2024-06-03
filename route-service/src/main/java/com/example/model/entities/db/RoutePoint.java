package com.example.model.entities.db;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "route_point")
public class RoutePoint extends BaseEntity {
    @NotNull
    private String name;

    @NotNull
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private String url;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date date;

    @NotNull
    private String startTime;

    @NotNull
    private String endTime;

    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;
}
