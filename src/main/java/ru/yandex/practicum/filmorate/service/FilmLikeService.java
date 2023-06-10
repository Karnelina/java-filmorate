package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.storage.dbStorage.event.EventStorage;
import ru.yandex.practicum.filmorate.storage.dbStorage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dbStorage.filmLike.FilmLikeDbStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmLikeService {
    private final FilmLikeDbStorage filmLikeStorage;

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
}