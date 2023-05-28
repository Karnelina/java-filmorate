package ru.yandex.practicum.filmorate.storage.dbStorage.filmLike;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.storage.dbStorage.film.FilmStorage;

import java.util.Collection;

@Repository
@Slf4j
public class FilmLikeDbStorage implements FilmLikeStorage {
    private final FilmLikeDao filmLikeDao;

    private final FilmStorage filmStorage;

    public FilmLikeDbStorage(FilmLikeDao filmLikeDao, FilmStorage filmStorage) {
        this.filmLikeDao = filmLikeDao;
        this.filmStorage = filmStorage;
    }

    @Override
    public FilmLike createLike(FilmLike filmLike) {
        filmLikeDao.createLike(filmLike);
        log.info("Создан лайк: " + filmLike);
        return filmLike;
    }

    @Override
    public Collection<FilmLike> getFilmLikes(long filmId) {
        return filmLikeDao.getFilmLikes(filmId);
    }

    @Override
    public void unlike(FilmLike filmLike) {
        filmStorage.getFilmById(filmLike.getFilmId()).orElseThrow(FilmNotFoundException::new);
        filmLikeDao.unlike(filmLike);
        log.info("Удален лайк: " + filmLike);
    }
}