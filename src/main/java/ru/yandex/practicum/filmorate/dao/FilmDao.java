package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface FilmDao {
    Optional<Film> addFilm(Film film);

    Optional<Film> updateFilm(Film film);

    Collection<Film> getAllFilms();

    Optional<Film> getFilmById(long id);

    void deleteFilm(long id);

    Set<Film> getFilmsIdsByUserId(long userId, long friendId);

    Collection<Film> getFilmsDirectorSorted(Integer directorId, String sortBy);

    Collection<Film> searchPopularFilmsByDirectorAndTitle(String query, String by);

}