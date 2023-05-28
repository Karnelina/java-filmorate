package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.FilmLike;

import java.util.Collection;

public interface FilmLikeDao {
    long createLike(FilmLike filmLike);

    Collection<FilmLike> getFilmLikes(long filmId);

    void unlike(FilmLike filmLike);
}
