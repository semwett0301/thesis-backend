package com.example.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "route")
public class Route extends BaseEntity {
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date start_date;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date end_date;

    @NotNull
    @Min(5000)
    private double transport_price;

    @NotNull
    @Min(5000)
    private double accommodation_price;

    private String additional_information;

    @NotNull
    @Column(columnDefinition = "boolean default false")
    private Boolean is_saved;

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
