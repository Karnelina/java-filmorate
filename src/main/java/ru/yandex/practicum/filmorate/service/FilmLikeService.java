package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.storage.dbStorage.event.EventStorage;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dbStorage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dbStorage.filmLike.FilmLikeDbStorage;
import ru.yandex.practicum.filmorate.storage.dbStorage.genre.GenreDbStorage;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmLikeService {
    private final FilmLikeDbStorage filmLikeStorage;
    private final GenreDbStorage genreStorage;

    private final FilmStorage filmStorage;

    private final UserService userService;

    private final EventStorage eventStorage;

    public void createLike(long filmId, long userId) {
        filmLikeStorage.createLike(FilmLike.builder()
                .filmId(filmId)
                .userId(userId)
                .build());
        eventStorage.addEvent(userId, filmId, "LIKE", "ADD");
    }

    public void unlike(long filmId, long userId) {
        userService.getUserById(userId);
        filmLikeStorage.unlike(FilmLike.builder()
                .filmId(filmId)
                .userId(userId)
                .build());
        eventStorage.addEvent(userId, filmId, "LIKE", "REMOVE");
    }

    public Collection<Film> getMostPopularFilms(int count) {
        Comparator<Film> comparator = Comparator.comparingInt(film -> film.getLikes().size());
        return filmStorage.getAllFilms()
                .stream()
                .peek(el -> {
                    List<FilmLike> likes = (List<FilmLike>) filmLikeStorage.getFilmLikes(el.getId());
                    if (!CollectionUtils.isEmpty(likes)) {
                        el.setLikes(likes.stream()
                                .map(FilmLike::getFilmId)
                                .collect(Collectors.toList()));
                    }
                })
                .sorted(comparator.reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public Collection<Film> getMostPopularFilmsByParams(Map<String, String> params) {
        List<Film> result = new ArrayList<>();
        if (params.containsKey("genreId") && params.containsKey("year")) {
            long id = Long.parseLong(params.get("genreId"));
            Genre optionalGenre = Optional.ofNullable(genreStorage.getGenreById(id)
                    .orElseThrow(GenreNotFoundException::new)).get();
            Predicate<Film> gen = film -> film.getGenres().stream()
                    .anyMatch(genre -> film.getGenres().contains(optionalGenre));
            Predicate<Film> releaseYear = film -> film.getReleaseDate().getYear() == Integer.parseInt(params.get("year"));
            result.addAll(filmStorage.getAllFilms()
                    .stream()
                    .filter(releaseYear.and(gen))
                    .collect(Collectors.toList()));
            return result.stream()
                    .limit(Integer.parseInt(params.getOrDefault("limit", "10")))
                    .collect(Collectors.toList());
        }

        if (params.containsKey("genreId")) {
            long id = Long.parseLong(params.get("genreId"));
            Genre optionalGenre = Optional.ofNullable(genreStorage.getGenreById(id).orElseThrow(GenreNotFoundException::new)).get();
            Predicate<Film> gen = film -> film.getGenres().stream()
                    .anyMatch(genre -> film.getGenres().contains(optionalGenre));
            result.addAll(filmStorage.getAllFilms().stream()
                    .filter(gen)
                    .collect(Collectors.toList()));
        } else if (params.containsKey("year")) {
            Predicate<Film> releaseYear = film -> film.getReleaseDate().getYear() == Integer.parseInt(params.get("year"));
            result.addAll(filmStorage.getAllFilms().stream()
                    .filter(releaseYear)
                    .collect(Collectors.toList()));
        }
        return result.stream()
                .limit(Integer.parseInt(params.getOrDefault("limit", "10")))
                .collect(Collectors.toList());
    }
}