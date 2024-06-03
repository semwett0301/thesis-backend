package com.example.model.exceptions;

public class IncorrectGptAnswerException extends Exception {
    public IncorrectGptAnswerException(String message) {
        super(message);
    }
}
