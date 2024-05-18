package com.example.model.dto.external.gpt;

import lombok.Data;

import java.util.List;

@Data
public class GptResponse {
    private Result result;

    @Data
    public static class Result {
        private List<MessageWrapper> alternatives;
    }

    @Data
    public static class MessageWrapper {
        private GptMessage message;
    }
}
