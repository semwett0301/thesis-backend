package com.example.model.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class AviasalesInternalRequest {
    private String destination;
    private String origin;
    private Date departureAt;
    private Date returnAt;
    private Integer limit;
    private Integer page;
    private Boolean oneWay;
}
