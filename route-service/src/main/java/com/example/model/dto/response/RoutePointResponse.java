package com.example.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoutePointResponse {
    private UUID id;
    private String name;
    private String description;
    private CoordsResponse coords;
    private String url;
    private Date date;
    private String start_time;
    private String end_time;
}
