package ru.yandex.practicum.filmorate.controller.exception;

public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
