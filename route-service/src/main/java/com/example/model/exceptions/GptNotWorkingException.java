package com.example.model.exceptions;

public class GptNotWorkingException extends Exception {
    @Override
    public String getMessage() {
        return "GPT model doesn't send a route";
    }
}
