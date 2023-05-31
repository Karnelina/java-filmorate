package ru.yandex.practicum.filmorate.storage.inMemoryStorage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class InMemoryFilmStorage implements InMemFilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private long id;

    private Long countId() {
        return ++id;
    }

    @Override
    public Film addFilm(Film film) throws ValidationException {

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 25))) {

            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }

        film.setId(countId());

        films.put(film.getId(), film);

        log.info("Фильм {} добавлен", film);

        return film;
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 25))) {

            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }

        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);

            log.info("Фильм {} обновлен", film);

            return film;

        } else {

            throw new FilmNotFoundException("Такого id не существует");
        }
    }

    @Override
    public Film getFilm(long idFilm) {
        if (films.containsKey(idFilm)) {
            return films.get(idFilm);
        }

        throw new FilmNotFoundException("Такого id не существует");
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    public void deleteFilm(long id) {
        log.info("Удаление фильма {}", id);

        films.remove(id);
    }
}