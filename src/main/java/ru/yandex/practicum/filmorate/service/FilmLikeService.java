package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.storage.dbStorage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dbStorage.filmLike.FilmLikeDbStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmLikeService {
    private final FilmLikeDbStorage filmLikeStorage;

    private final FilmStorage filmStorage;

    private final UserService userService;

    public void createLike(long filmId, long userId) {
        filmLikeStorage.createLike(FilmLike.builder()
                .filmId(filmId)
                .userId(userId)
                .build());
    }

    public void unlike(long filmId, long userId) {
        userService.getUserById(userId);
        filmLikeStorage.unlike(FilmLike.builder()
                .filmId(filmId)
                .userId(userId)
                .build());
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

    public Collection<Film> getMostPopularFilmsByParams(Integer limit, Long genreId, Integer year) {
        return filmStorage.getAllFilms()
                .stream()
                .filter(film -> film.getReleaseDate().getYear() == year && film.getGenres().contains(genreId))
                .limit(limit)
                .collect(Collectors.toList());
    }
}