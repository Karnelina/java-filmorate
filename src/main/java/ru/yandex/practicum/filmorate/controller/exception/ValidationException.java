package ru.yandex.practicum.filmorate.controller.exception;

public class ValidationException extends Throwable {
    public ValidationException(String message) {
        super(message);
    }
}
