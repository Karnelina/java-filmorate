package ru.yandex.practicum.filmorate.dao;

public interface FilmGenreDao {
    void linkGenreToFilm(long filmId, long genreId);

    void deleteFilmGenres(long filmId);

    boolean linkAlreadyExist(long filmId, long genreId);
}
