package ru.yandex.practicum.filmorate.storage.dbStorage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Optional<Film> updateFilm(Film film);

    Optional<Film> createFilm(Film film);

    Optional<Film> getFilmById(long id);

    void deleteFilm(long id);

    Set<Film> getCommonFilms(long userId, long friendId);

    Collection<Film> getFilmsDirectorSorted(Integer directorId, String sortBy);

    Collection<Film> searchPopularFilmsByDirectorAndTitle(String query, String by);
}