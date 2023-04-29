package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
public class FilmController {

    FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }


    @PostMapping(value = "/films")
    @ResponseBody
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Получен запрос Post film.");

        return filmService.addFilm(film);
    }

    @PutMapping(value = "/films")
    @ResponseBody
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Получен запрос Put film.");

        return filmService.updateFilm(film);
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        log.info("Получен запрос Get films.");

        return filmService.getAllFilms();
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable("id") long filmId) {
        return filmService.getFilm(filmId);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike(@PathVariable("id") long idFilm,
                        @PathVariable("userId") long idUser) throws ValidationException {
        log.info("Получен запрос Put like.");
        return filmService.addLike(idFilm, idUser);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") long idFilm,
                           @PathVariable("userId") long idUser) throws ValidationException {
        log.info("Получен запрос Delete like.");
        filmService.deleteLike(idFilm, idUser);
    }

    @GetMapping("/films/popular")
    public Collection<Film> getMostPopularFilms(@RequestParam(required = false) Optional<Integer> count) {
        log.info("Получен запрос Get most popular films.");
        if (count.isPresent()) {
            return filmService.getMostPopularFilms(count.get());
        }
        return filmService.getMostPopularFilms(10);
    }

}