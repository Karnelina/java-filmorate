package ru.yandex.practicum.filmorate.storage.dbStorage.filmLike;

import ru.yandex.practicum.filmorate.model.FilmLike;

import java.util.Collection;

public interface FilmLikeStorage {
    FilmLike createLike(FilmLike filmLike);

    Collection<FilmLike> getFilmLikes(long filmId);

    void unlike(FilmLike filmLike);
}