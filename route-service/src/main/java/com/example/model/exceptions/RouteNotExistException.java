package com.example.model.exceptions;

public class RouteNotExistException extends Exception {
    @Override
    public String getMessage() {
        return "Route doesn't exist";
    }
}
