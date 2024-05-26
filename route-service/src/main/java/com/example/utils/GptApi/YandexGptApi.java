package com.example.utils.GptApi;

import com.example.model.external.gpt.GptBadRequestError;
import com.example.model.external.gpt.GptRequest;
import com.example.model.external.gpt.GptResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class YandexGptApi implements GptApi {
    private final WebClient webClient;

    private final String uri;

    private final String token;

    public YandexGptApi(WebClient webClient, @Value("${yandex-gpt.uri}") String uri, @Value("${yandex-gpt.iam-token}") String token) {
        this.webClient = webClient;
        this.uri = uri;
        this.token = token;
    }

    @Override
    public GptResponse getAnswerFromGpt(GptRequest gptRequest) {
        return webClient
                .post()
                .uri(uri)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(gptRequest), GptRequest.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    response.bodyToMono(GptBadRequestError.class).subscribe(body -> {
                        assert body != null;
                        log.error(String.valueOf(body.getError().getMessage()));
                    });
                    return Mono.error(new Exception(response.statusCode().toString()));
                })
                .bodyToMono(GptResponse.class)
                .block();
    }
}
