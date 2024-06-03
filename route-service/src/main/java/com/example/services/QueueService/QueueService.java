package com.example.services.QueueService;

import com.example.model.dto.response.RouteResponse;
import com.example.model.exceptions.GptNotWorkingException;
import com.example.model.exceptions.RouteNotExistException;

import java.io.Serializable;

public interface QueueService {
    void executeGenerateTask(String routeResponse) throws RouteNotExistException, GptNotWorkingException, InterruptedException;
}
