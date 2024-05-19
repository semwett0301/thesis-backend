package com.example.services.GenerateService;

import com.example.model.dto.external.gpt.GptMessage;
import com.example.model.dto.external.gpt.GptRequest;
import com.example.model.dto.external.gpt.GptRole;
import com.example.model.dto.internal.GeneratedRoutePoint;
import com.example.model.dto.request.RouteRequest;
import com.example.model.exceptions.GptNotWorkingException;
import com.example.utils.GptApi.GptApi;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class YandexGenerateService implements GenerateService {
    private final String gptContext = "Ты ассистент по путешествиям. Ты умеешь формировать маршруты от точки к точке по городу по часам, при этом учитывая последовательность времени и расстояние между точками. Все ответы ты отдаешь только в JSON формате и не умеешь писать обычный текст";

    private final GptApi gptApi;
    private final Gson gson;

    private final String modelUri;

    private final Boolean stream;

    private final Double temperature;

    private final Integer maxTokens;

    public YandexGenerateService(GptApi gptApi, Gson gson,
                                 @Value("${yandex-gpt.model-uri}") String modelUri,
                                 @Value("#{new Boolean('${yandex-gpt.stream}')}") Boolean stream,
                                 @Value("#{new Double('${yandex-gpt.temperature}')}") Double temperature,
                                 @Value("#{new Integer('${yandex-gpt.maxTokens}')}") Integer maxTokens) {
        this.gptApi = gptApi;
        this.gson = gson;
        this.modelUri = modelUri;
        this.stream = stream;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
    }

    @Override
    public List<GeneratedRoutePoint> generate(RouteRequest request) throws GptNotWorkingException {

        var completionOptions = new GptRequest.CompletionOptions(stream, temperature, maxTokens);
        var requestMessages = getRequestMessages(request);
        var gptRequest = new GptRequest(modelUri, completionOptions, requestMessages);
        var gptResponse = gptApi.getAnswerFromGpt(gptRequest);


        var gptResponseMessage = gptResponse.getResult().getAlternatives().get(0);

        if (gptResponseMessage != null) {
            var message = gptResponseMessage.getMessage().getText();
            var json = message.substring(message.lastIndexOf('['), message.lastIndexOf(']') + 1);
            var pointsArray = gson.fromJson(json, GeneratedRoutePoint[].class);

            return List.of(pointsArray);
        }

        throw new GptNotWorkingException();
    }

    private List<GptMessage> getRequestMessages(RouteRequest request) {
        final List<GptMessage> result = new ArrayList<>();

        final GptMessage systemMessage = new GptMessage(GptRole.SYSTEM, gptContext);
        final GptMessage userMessage = new GptMessage(GptRole.SYSTEM, getUserMessage(request));

        result.add(systemMessage);
        result.add(userMessage);

        return result;
    }

    private String getUserMessage(RouteRequest request) {
        DateFormat df = new SimpleDateFormat("dd.MM");
        StringBuilder resultBuilder = new StringBuilder();

        resultBuilder.append("Составь маршрут по городу ");
        resultBuilder.append(request.getEnd_city().getName());

        resultBuilder.append(" c ");
        resultBuilder.append(df.format(request.getStart_date()));

        resultBuilder.append(" по ");
        resultBuilder.append(df.format(request.getEnd_date()));

        resultBuilder.append(" с 5 точками маршрута.");

        if (request.getAdditional_information() != null) {
            resultBuilder.append(" Учти следующие пожелания: ");
            resultBuilder.append(request.getAdditional_information());
        }
        resultBuilder.append(" В ответе пришли массив из точек маршрута в формате JSON. Структура точки маршрута является следующей: { 'name': 'название точки маршрута', 'description': 'максимально подробное описание точки маршрута', 'latitude': 'Координата долготы для точки маршрута', 'longitude': 'Координата широты для точки маршрута', 'url': 'Ссылка на ресурс с описанием точки маршрута', 'date': 'Дата посещения точки в формате UTC', 'startTime': 'Время начала посещения точки в формате HH:mm','endTime': 'Время окончания посещения точки в формате HH:mm'}");

        return resultBuilder.toString();
    }
}
