package com.example.model.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class GeneratedRoutePoint {
    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
    private String url;
    private Date date;
    private String startTime;
    private String endTime;
}
