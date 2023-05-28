package ru.yandex.practicum.filmorate.storage.dbStorage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreStorage {
    Collection<Genre> getGenres();

    Optional<Genre> getGenreById(long genreId);

    Collection<Genre> getGenresByFilmId(long filmId);
}