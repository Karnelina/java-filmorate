package ru.yandex.practicum.filmorate.storage.inMemoryStorage.film;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface InMemFilmStorage {
    Film addFilm(Film film) throws ValidationException;

    Film updateFilm(Film film) throws ValidationException;

    Film getFilm(long idFilm);

    List<Film> getAllFilms();

    void deleteFilm(long id);
}