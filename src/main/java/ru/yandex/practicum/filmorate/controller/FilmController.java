package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    int id = 0;

    private Integer countId() {
        return ++id;
    }

    @PostMapping(value = "/films")
    @ResponseBody
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Получен запрос Post film.");

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 25))) {
            log.warn("Дата релиза — не раньше 28 декабря 1895 года");

            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }

        Film movie = Film.builder()
                .id(countId())
                .name(film.getName())
                .description(film.getDescription())
                .duration(film.getDuration())
                .releaseDate(film.getReleaseDate())
                .build();

        if (films.containsKey(movie.getId())) {
            log.warn("Такой Id уже добавлен");

            throw new ValidationException("Такой Id уже добавлен");
        }

        films.put(movie.getId(), movie);

        log.info("Фильм {} добавлен", movie);

        return movie;
    }

    @PutMapping(value = "/films")
    @ResponseBody
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Получен запрос Put film.");


        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 25))) {
            log.warn("Дата релиза — не раньше 28 декабря 1895 года");

            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }

        if (films.containsKey(film.getId())) {
            films.get(film.getId()).setName(film.getName());
            films.get(film.getId()).setDescription(film.getDescription());
            films.get(film.getId()).setReleaseDate(film.getReleaseDate());
            films.get(film.getId()).setDuration(film.getDuration());

            log.info("Фильм {} добавлен", film);

            return film;
        } else {
            log.warn("Такого id не существует");

            throw new ValidationException("Такого id не существует");
        }
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        log.info("Получен запрос Get films.");

        return new ArrayList<>(films.values());
    }
}