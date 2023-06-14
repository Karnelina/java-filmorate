package ru.yandex.practicum.filmorate.storage.dbStorage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.FilmDirectorDao;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dbStorage.director.DirectorDbStorage;
import ru.yandex.practicum.filmorate.storage.dbStorage.genre.GenreDbStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Primary
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final FilmDao filmDao;

    private final GenreDbStorage genreStorage;
    private final DirectorDbStorage directorStorage;

    private final FilmGenreDao filmGenreDao;

    private final FilmDirectorDao filmDirectorDao;

    public FilmDbStorage(FilmDao filmDao, GenreDbStorage genreStorage, FilmGenreDao filmGenreDao, FilmDirectorDao filmDirectorDao,
                         DirectorDbStorage directorStorage) {
        this.filmDao = filmDao;
        this.genreStorage = genreStorage;
        this.filmGenreDao = filmGenreDao;
        this.filmDirectorDao = filmDirectorDao;
        this.directorStorage = directorStorage;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return filmDao.getAllFilms();
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        filmGenreDao.deleteFilmGenres(film.getId());
        filmDirectorDao.deleteFilmDirectors(film.getId());
        film = filmDao.updateFilm(film).get();
        if (!CollectionUtils.isEmpty(film.getGenres())) {
            for (Genre genre : film.getGenres()) {
                filmGenreDao.linkGenreToFilm(film.getId(), genre.getId());
            }
        }
        if (!CollectionUtils.isEmpty(film.getDirectors())) {
            filmDirectorDao.linkDirectorToFilm(film);
        }

        film.setGenres((List<Genre>) genreStorage.getGenresByFilmId(film.getId()));
        film.setDirectors((List<Director>) directorStorage.getDirectorsByFilmId(film.getId()));
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

    @Override
    public Set<Film> getCommonFilms(long userId, long friendId) {
        Set<Film> films = filmDao.getFilmsIdsByUserId(userId, friendId);

        log.info("Количество фильмов: " + films.size());
        return films;
    }

    public Collection<Film> getFilmsDirectorSorted(Integer directorId, String sortBy) {
        return filmDao.getFilmsDirectorSorted(directorId, sortBy);
    }

    @Override
    public Collection<Film> searchPopularFilmsByDirectorAndTitle(String query, String by) {
        return filmDao.searchPopularFilmsByDirectorAndTitle(query, by);
    }

}