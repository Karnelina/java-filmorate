package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmDirectorDao {
    void linkDirectorToFilm(Film film);

    void deleteFilmDirectors(long filmId);
}