package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmDao {
    Optional<Film> addFilm(Film film);

    Optional<Film> updateFilm(Film film);

    Collection<Film> getAllFilms();

    Optional<Film> getFilmById(long id);

    void deleteFilm(long id);
}