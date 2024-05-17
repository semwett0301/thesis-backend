package com.example.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "city")
public class City {
    @Id
    private String iata;

    @NotNull
    private String name;
}
