package com.example.model.exceptions;

public class AviasalesException extends Exception {
    @Override
    public String getMessage() {
        return "Aviasales couldn't find tickets";
    }
}
