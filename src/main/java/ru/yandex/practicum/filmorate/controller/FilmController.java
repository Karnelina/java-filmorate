package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmLikeService;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    private final FilmLikeService filmLikeService;

    public FilmController(FilmService filmService, FilmLikeService filmLikeService) {
        this.filmService = filmService;
        this.filmLikeService = filmLikeService;
    }

    @GetMapping()
    public Collection<Film> getFilms() {
        return filmService.getFilms();
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PostMapping()
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable long filmId) {
        return filmService.getFilmById(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void createLike(@PathVariable long filmId, @PathVariable long userId) {
        filmLikeService.createLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void unlike(@PathVariable long filmId, @PathVariable long userId) {
        filmLikeService.unlike(filmId, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getMostPopularFilms(@RequestParam(required = false) Optional<Integer> count) {
        if (count.isPresent()) {
            return filmLikeService.getMostPopularFilms(count.get());
        }
        return filmLikeService.getMostPopularFilms(10);
    }

    @DeleteMapping("/films/{id}/delete")
    public void deleteFilm(@PathVariable("id") long id) {
        filmService.deleteFilm(id);
    }
}