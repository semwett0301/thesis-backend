package com.example.model.entity;

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
    private String description;

    @NotNull
    private Integer latitude;

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
