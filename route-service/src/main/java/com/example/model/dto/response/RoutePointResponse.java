package com.example.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class RoutePointResponse {
    private UUID id;
    private String name;
    private String description;
    private CoordsResponse coords;
    private String url;
    private Date date;
    private String startTime;
    private String endTime;
}
