package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dbStorage.genre.GenreStorage;

import java.util.Collection;

@Service
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;

    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Genre getGenreById(long genreId) {
        var genre = genreStorage.getGenreById(genreId);
        if (genre.isPresent()) {
            log.info("Получен жанр: " + genre);

            return genre.get();
        }
        throw new GenreNotFoundException("Жанр с таким id не существует");

    }

    public Collection<Genre> getGenres() {
        Collection<Genre> genres = genreStorage.getGenres();
        log.info("Получены жанры: " + genres);
        return genres;
    }
}