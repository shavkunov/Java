package ru.spbau.shavkunov.hw4;

public class NotPresentException extends RuntimeException {
    private String message;

    public NotPresentException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}