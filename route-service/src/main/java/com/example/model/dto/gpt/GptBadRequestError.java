package com.example.model.dto.gpt;

import lombok.Data;

import java.util.List;

@Data
public class GptBadRequestError {
    private ErrorDetails error;

    @Data
    public static class ErrorDetails {
        private int grpcCode;
        private int httpCode;
        private String message;
        private String httpStatus;
        private List<Object> details;
    }
}
