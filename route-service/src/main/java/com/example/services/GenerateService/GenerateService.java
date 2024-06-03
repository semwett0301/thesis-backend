package com.example.services.GenerateService;

import com.example.model.dto.internal.GeneratedRoutePoint;
import com.example.model.entities.db.Route;
import com.example.model.exceptions.GptNotWorkingException;
import com.example.model.exceptions.IncorrectGptAnswerException;

import java.util.List;

public interface GenerateService {
    List<GeneratedRoutePoint> generate(Route route) throws GptNotWorkingException, IncorrectGptAnswerException;
}
