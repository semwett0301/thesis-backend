package com.example.services.GenerateService;

import com.example.model.dto.gpt.GptMessage;
import com.example.model.dto.gpt.GptRequest;
import com.example.model.dto.gpt.GptResponse;
import com.example.model.dto.gpt.GptRole;
import com.example.model.dto.internal.GeneratedRoutePoint;
import com.example.model.entities.db.Route;
import com.example.model.exceptions.GptNotWorkingException;
import com.example.model.exceptions.IncorrectGptAnswerException;
import com.example.utils.GptApi.GptApi;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class YandexGenerateService implements GenerateService {
    private final GptApi gptApi;
    private final Gson gson;

    private final String modelUri;

    private final GptRequest.CompletionOptions completionOptions;

    private final DateFormat localDf = new SimpleDateFormat("yyyy-MM-dd");
    private final DateTimeFormatter dayDf = DateTimeFormatter.ofPattern("dd.MM");

    public YandexGenerateService(GptApi gptApi, Gson gson,
                                 @Value("${yandex-gpt.model-uri}") String modelUri,
                                 @Value("#{new Boolean('${yandex-gpt.stream}')}") Boolean stream,
                                 @Value("#{new Double('${yandex-gpt.temperature}')}") Double temperature,
                                 @Value("#{new Integer('${yandex-gpt.maxTokens}')}") Integer maxTokens) {
        this.gptApi = gptApi;
        this.gson = gson;
        this.modelUri = modelUri;
        this.completionOptions = new GptRequest.CompletionOptions(stream, temperature, maxTokens);
    }

    @Override
    public List<GeneratedRoutePoint> generate(Route route) throws GptNotWorkingException, IncorrectGptAnswerException {
        final var requestMessages = getStartMessages(route);

        var gptRequest = new GptRequest(modelUri, completionOptions, requestMessages);
        var gptResponse = gptApi.getAnswerFromGpt(gptRequest);

        final var firstRoutePoints = getRoutePointsFromMessage(gptResponse.getFirstMessage().orElseThrow(GptNotWorkingException::new));
        final List<GeneratedRoutePoint> result = new ArrayList<>(firstRoutePoints);

        final LocalDate startLocalDate = LocalDate.parse(localDf.format(route.getStartDate()));
        final LocalDate endLocalDate = LocalDate.parse(localDf.format(route.getEndDate()));

        for (LocalDate date = startLocalDate.plusDays(1); date.isBefore(endLocalDate.plusDays(1)); date = date.plusDays(1)) {
            var continueRequestMessage = getContinueUserMessage(date);
            requestMessages.add(gptResponse.getFirstMessage().orElseThrow(GptNotWorkingException::new).getMessage());
            requestMessages.add(new GptMessage(GptRole.USER, continueRequestMessage));

            gptRequest = new GptRequest(modelUri, completionOptions, requestMessages);
            gptResponse = gptApi.getAnswerFromGpt(gptRequest);
            result.addAll(getRoutePointsFromMessage(gptResponse.getFirstMessage().orElseThrow(GptNotWorkingException::new)));
        }

        return result;
    }

    private List<GeneratedRoutePoint> getRoutePointsFromMessage(GptResponse.MessageWrapper message) throws IncorrectGptAnswerException {
        try {
            var json = prepareJson(message.getMessage().getText());
            return List.of(gson.fromJson(json, GeneratedRoutePoint[].class));
        } catch (JsonSyntaxException e) {
            throw new IncorrectGptAnswerException(e.getMessage());
        }
    }

    private String prepareJson(String initialText) {
        var leftIndex = initialText.lastIndexOf('[');
        var rightIndex = initialText.lastIndexOf(']');

        var leftBorder = leftIndex != -1 ? leftIndex : 0;
        var rightBorder = rightIndex != -1 ? rightIndex + 1 : initialText.length();

        return initialText.substring(leftBorder, rightBorder)
                .replaceAll("'", "\"")
                .replaceAll("»,", "\",")
                .replaceAll("»", "")
                .replaceAll("«", "")
                .replaceAll("\"+", "\"")
                .replaceAll("\n*", "")
                .replaceAll("\t+", "")
                .replaceAll("\s+\\{\s+", "{")
                .replaceAll("\s+}\s+", "}")
                .replaceAll("\s+\"", "\"")
                .replaceAll("\"\s+", "\"");
    }

    private List<GptMessage> getStartMessages(Route route) {
        final List<GptMessage> result = new ArrayList<>();

        final String gptContext = "Ты ассистент по путешествиям. Ты умеешь формировать маршруты от точки к точке по городу по часам, при этом учитывая последовательность времени и расстояние между точками. Все ответы ты отдаешь только в JSON формате и не умеешь писать обычный текст";

        final Optional<String> additional = route.getAdditionalInformation() != null ? Optional.of(route.getAdditionalInformation()) : Optional.empty();

        final GptMessage systemMessage = new GptMessage(GptRole.SYSTEM, gptContext);
        final GptMessage userMessage = new GptMessage(GptRole.USER, getStartUserMessage(LocalDate.parse(localDf.format(route.getStartDate())),
                route.getEndCity().getName(),
                additional));

        result.add(systemMessage);
        result.add(userMessage);

        return result;
    }

    private String getContinueUserMessage(LocalDate date) {
        final var day = date.format(dayDf);

        return MessageFormat.format("Сгенерируй ещё 4 точки маршрута на {0} с учетом тех же пожеланий и в том же формате", day);
    }

    private String getStartUserMessage(LocalDate date, String city, Optional<String> additionalInformation) {
        final var day = date.format(dayDf);

        final var mainText = MessageFormat.format("Составь маршрут по городу {0} на {1}. Приведи минимум 4 точки для посещения.", city, day);

        var additionalText = "";
        if (additionalInformation.isPresent()) {
            var info = additionalInformation.get().replaceAll("\n+", " ");
            additionalText = MessageFormat.format(" Учти следующие пожелания: {0}.", info.toLowerCase());
        }

        final var ending = " В ответе пришли массив из точек маршрута в формате JSON. Структура точки маршрута является следующей: { 'name': 'название точки маршрута', 'description': 'максимально подробное описание точки маршрута', 'latitude': 'Координата долготы для точки маршрута', 'longitude': 'Координата широты для точки маршрута', 'url': 'Ссылка на ресурс с описанием точки маршрута', 'date': 'Дата посещения точки в формате UTC', 'startTime': 'Время начала посещения точки в формате HH:mm','endTime': 'Время окончания посещения точки в формате HH:mm'}.";

        return mainText + additionalText + ending;
    }
}
