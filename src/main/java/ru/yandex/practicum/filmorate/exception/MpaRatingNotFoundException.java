package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class MpaRatingNotFoundException extends RuntimeException {

    public MpaRatingNotFoundException(final String message) {
        super(message);
    }

    public MpaRatingNotFoundException() {
        super();
    }
}
