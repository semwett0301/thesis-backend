package com.example.model.dto.external.gpt;

import lombok.Data;
import org.apache.commons.lang.enums.Enum;

import java.util.List;
import java.util.Optional;

@Data
public class GptResponse {
    private Result result;

    public Optional<MessageWrapper> getFirstMessage() {
        final var message = this.getResult().getAlternatives().get(0);
        return message != null ? Optional.of(message) : Optional.empty();
    }

    @Data
    public static class Result {
        private List<MessageWrapper> alternatives;
    }

    @Data
    public static class MessageWrapper {
        private GptMessage message;
    }
}
