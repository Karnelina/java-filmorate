package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.filmorate.dao.FilmDirectorDao;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dbStorage.film.FilmStorage;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    private final FilmGenreDao filmGenreDao;
    private final FilmDirectorDao filmDirectorDao;

    public Film getFilmById(long filmId) {
        Film film = filmStorage.getFilmById(filmId).orElseThrow(FilmNotFoundException::new);
        log.info("Получен фильм id = " + filmId);
        return film;
    }

    public Collection<Film> getFilms() {
        Collection<Film> films = filmStorage.getAllFilms();
        if (CollectionUtils.isEmpty(films)) {
            log.info("Получены фильмы: " + films);
            return films;
        }
        log.info("Получен пустой список фильмов");
        return new ArrayList<>(films);
    }

    public Film updateFilm(Film film) {
        if (!isExist(film.getId())) {
            log.error("Ошибка, фильм не найден: " + film);
            throw new FilmNotFoundException();
        }
        return filmStorage.updateFilm(film).get();
    }

    public Film createFilm(Film film) {
        filmStorage.createFilm(film);
        if (!CollectionUtils.isEmpty(film.getGenres())) {
            for (Genre genre : film.getGenres()) {
                filmGenreDao.linkGenreToFilm(film.getId(), genre.getId());
            }
        }
        if (!CollectionUtils.isEmpty(film.getDirectors())) {
            filmDirectorDao.linkDirectorToFilm(film);
        }
        log.info("Фильм {} создан.", film.getName());
        return film;
    }

    private boolean isExist(long id) {
        return filmStorage.getFilmById(id).isPresent();
    }

    public void deleteFilm(long id) {
        filmStorage.deleteFilm(id);
    }

    public Collection<Film> getFilmsDirector(Integer directorId, String sortBy) {
        Collection<Film> films = filmStorage.getFilmsDirectorSorted(directorId, sortBy);
        if (CollectionUtils.isEmpty(films)) {
            log.info("Получены фильмы: " + films);
            return films;
        }
        log.info("Получен пустой список фильмов");
        return new ArrayList<>(films);
    }

    public Collection<Film> searchPopularFilmsByDirectorAndTitle(String query, String by) {
        Collection<Film> films = filmStorage.searchPopularFilmsByDirectorAndTitle(query, by);
        if (!CollectionUtils.isEmpty(films)) {
            log.info("Получены фильмы: " + films);
            return films;
        }
        log.info("Получен пустой список фильмов");
        return new ArrayList<>(films);
    }
}