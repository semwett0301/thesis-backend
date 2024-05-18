package com.example.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "city")
public class City {
    @Id
    private String iata;

    @NotNull
    private String name;
}
