package ru.yandex.practicum.filmorate.storage.dbStorage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Optional<Film> updateFilm(Film film);

    Optional<Film> createFilm(Film film);

    Optional<Film> getFilmById(long id);

    void deleteFilm(long id);

    Collection<Film> getFilmsDirectorSorted(Integer directorId, String sortBy);

    Collection<Film> searchPopularFilmsByDirectorAndTitle(String query, String by);
}