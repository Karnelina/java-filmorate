package ru.yandex.practicum.filmorate.storage.dbStorage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

@Repository
@Slf4j
public class GenreDbStorage implements GenreStorage {
    private final GenreDao genreDao;

    public GenreDbStorage(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public Collection<Genre> getGenres() {
        return genreDao.getGenres();
    }

    @Override
    public Optional<Genre> getGenreById(long genreId) {
        return genreDao.getGenreById(genreId);
    }

    @Override
    public Collection<Genre> getGenresByFilmId(long filmId) {
        Collection<Genre> genres = genreDao.getGenresByFilmId(filmId);
        log.info("Получены жанры: " + genres);
        return genres;
    }
}