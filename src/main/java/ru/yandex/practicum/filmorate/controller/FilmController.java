package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.constant.SortingConstant;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.service.FilmLikeService;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    private final FilmLikeService filmLikeService;

    private final DirectorService directorService;

    public FilmController(FilmService filmService, FilmLikeService filmLikeService, DirectorService directorService) {
        this.filmService = filmService;
        this.filmLikeService = filmLikeService;
        this.directorService = directorService;
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

    @GetMapping(value = "/popular", params = {"genreId", "year"})
    public Collection<Film> getMostPopularFilmsByParams(@RequestParam Map<String, String> params) {
        return filmLikeService.getMostPopularFilmsByParams(params);
    }

    @GetMapping(value = "/popular", params = {"year"})
    public Collection<Film> getMostPopularFilmsByYears(@RequestParam Map<String, String> params) {
        return filmLikeService.getMostPopularFilmsByParams(params);
    }

    @GetMapping(value = "/popular", params = {"genreId"})
    public Collection<Film> getMostPopularFilmsByGenre(@RequestParam Map<String, String> params) {
        return filmLikeService.getMostPopularFilmsByParams(params);
    }

    @GetMapping("/popular")
    public Collection<Film> getMostPopularFilms(@RequestParam(required = false) Integer count) {
        return filmLikeService.getMostPopularFilms(Objects.requireNonNullElse(count, 10));
    }

    @GetMapping(value = "/common", params = {"userId", "friendId"})
    public Collection<Film> getCommonFilms(@RequestParam("userId") long userId,
                                           @RequestParam("friendId") long friendId) {
        log.info("запрос на получение общих с другом фильмов");
        return filmService.getCommonFilms(userId, friendId);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable long id) {
        filmService.deleteFilm(id);
    }

    @GetMapping("/director/{directorId}")
    public Collection<Film> listF(@PathVariable Integer directorId, @RequestParam String sortBy) {
        directorService.getDirectorById(directorId);
        if (!SortingConstant.SORTS.contains(sortBy)) {
            log.info("Ожидаем параметр сортировки year или likes, а получили: {}", sortBy);
            throw new RuntimeException(String.format("Ожидаем параметр сортировки year или likes, а получили: %s", sortBy));
        }
        log.info("Возвращаем список фильмов режиссера отсортированных по {}", sortBy);
        return filmService.getFilmsDirector(directorId, sortBy);

    }

    @GetMapping("/search")
    public Collection<Film> searchPopularFilmsByDirectorAndTitle(@RequestParam(defaultValue = "") String query,
                                                                 @RequestParam(defaultValue = "") String by) {
        log.info("Запрашиваем список популярных фильмов согласно установленному отбору");
        return filmService.searchPopularFilmsByDirectorAndTitle(query, by);
    }
}