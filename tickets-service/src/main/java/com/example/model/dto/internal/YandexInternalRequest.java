package com.example.model.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class YandexInternalRequest {
    private String from;
    private String to;
    private Date date;
}
