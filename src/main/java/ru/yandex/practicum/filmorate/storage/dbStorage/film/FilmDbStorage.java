package ru.yandex.practicum.filmorate.storage.dbStorage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dbStorage.genre.GenreDbStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final FilmDao filmDao;

    private final GenreDbStorage genreStorage;

    private final FilmGenreDao filmGenreDao;

    public FilmDbStorage(FilmDao filmDao, GenreDbStorage genreStorage, FilmGenreDao filmGenreDao) {
        this.filmDao = filmDao;
        this.genreStorage = genreStorage;
        this.filmGenreDao = filmGenreDao;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return filmDao.getAllFilms();
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        filmGenreDao.deleteFilmGenres(film.getId());
        film = filmDao.updateFilm(film).get();
        if (!CollectionUtils.isEmpty(film.getGenres())) {
            for (Genre genre : film.getGenres()) {
                filmGenreDao.linkGenreToFilm(film.getId(), genre.getId());
            }
        }
        if (CollectionUtils.isEmpty(film.getDirectors())) {
            film.setDirectors(new ArrayList<>());
        }
        film.setGenres((List<Genre>) genreStorage.getGenresByFilmId(film.getId()));
        log.info("Обновлен фильм: " + film);
        return Optional.of(film);
    }

    @Override
    public Optional<Film> createFilm(Film film) {
        return filmDao.addFilm(film);
    }

    @Override
    public Optional<Film> getFilmById(long id) {
        return filmDao.getFilmById(id);
    }

    @Override
    public void deleteFilm(long id) {
        filmDao.deleteFilm(id);
    }
}