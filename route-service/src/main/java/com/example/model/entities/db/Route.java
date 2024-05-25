package com.example.model.entities.db;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "route")
public class Route extends BaseEntity {
    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;

    @NotNull
    @Min(5000)
    @Column(name = "transport_price")
    private double transportPrice;

    @NotNull
    @Min(5000)
    @Column(name = "accommodation_price")
    private double accommodationPrice;

    @Column(name = "additional_information")
    private String additionalInformation;

    @NotNull
    @Column(name = "is_saved", columnDefinition = "boolean default false")
    private Boolean isSaved;

    @OneToMany
    @JoinColumn(name = "route_id")
    private List<RoutePoint> routePoints;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private UserInfo user;

    @ManyToOne
    @JoinColumn(name = "start_city_id", nullable = false)
    private City startCity;

    @ManyToOne
    @JoinColumn(name = "end_city_id", nullable = false)
    private City endCity;
}
