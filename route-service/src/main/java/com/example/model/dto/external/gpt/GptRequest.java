package com.example.model.dto.external.gpt;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class GptRequest implements Serializable {
    private String modelUri;
    private CompletionOptions completionOptions;
    private List<GptMessage> messages;

    @AllArgsConstructor
    @Getter
    @Setter
    public static class CompletionOptions {
        private boolean stream;

        @Max(1)
        private double temperature;

        @Min(100)
        @Max(3000)
        private int maxTokens;
    }
}