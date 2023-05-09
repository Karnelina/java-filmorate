package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    FilmController filmController;
    FilmService filmService;
    FilmStorage filmStorage;
    UserStorage userStorage;
    Film film1;
    Film oldFilm;
    Film unnamed;
    Film overDescription;
    Film minusDur;
    Film changeFilm;
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @BeforeEach
    void beforeEach() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        filmService = new FilmService(filmStorage, userStorage);
        filmController = new FilmController(filmService);

        film1 = new Film(
                1L,
                "Film",
                "Interesting Film",
                LocalDate.of(2022, 12, 20),
                Duration.ofMinutes(120)
        );

        oldFilm = new Film(
                2L,
                "Old Film",
                "Old Film",
                LocalDate.of(1895, 12, 24),
                Duration.ofMinutes(60)
        );

        unnamed = new Film(
                3L,
                "",
                "Interesting Film",
                LocalDate.of(2022, 12, 20),
                Duration.ofMinutes(120)
        );

        overDescription = new Film(
                4L,
                "Film",
                "Interesting Film jffokjfojjnjfgohgoidfosiegoiegosidjgosiegoisegofieoighosiegefglkjdlfkjd " +
                        "doifjoifgoisjdoiajofoasijdpioasjopfijoihfgohoaijsdf[j{Dfj[DIjfoigjh[oidjgoijdogjdfkdokfjdfdf" +
                        "fhjoksdjokdjfoksjdfoijdflksdjfosidjfosijdfosjdfosidjfosidjfosidfjosidjfsodifjsodfosidjfosijdf",
                LocalDate.of(2022, 12, 20),
                Duration.ofMinutes(120)
        );

        minusDur = new Film(
                5L,
                "Film",
                "Interesting Film",
                LocalDate.of(2022, 12, 20),
                Duration.ofMinutes(-120)
        );

        changeFilm = new Film(
                1L,
                "newFilm",
                "New interesting Film",
                LocalDate.of(2020, 10, 10),
                Duration.ofMinutes(30)
        );
    }


    @Test
    void addFilm() throws ValidationException {
        int size = filmController.getAllFilms().size();
        assertEquals(0, size, "Not null size");

        filmController.addFilm(film1);
        int size1 = filmController.getAllFilms().size();
        assertEquals(1, size1, "Отличается размер мапы");

        assertEquals(1, validator.validate(unnamed).size());
        assertEquals(1, validator.validate(overDescription).size());
        assertEquals(1, validator.validate(minusDur).size());
        assertEquals(1, validator.validate(unnamed).size());

        final ValidationException exception = assertThrows(
                ValidationException.class,

                () -> filmController.addFilm(oldFilm));

        assertEquals("Дата релиза — не раньше 28 декабря 1895 года",
                exception.getMessage());

    }

    @Test
    void updateFilm() throws ValidationException {
        int size = filmController.getAllFilms().size();
        assertEquals(0, size, "Not null size");

        filmController.addFilm(film1);
        int size1 = filmController.getAllFilms().size();
        assertEquals(1, size1, "Отличается размер мапы");

        assertEquals(1, validator.validate(unnamed).size());
        assertEquals(1, validator.validate(overDescription).size());
        assertEquals(1, validator.validate(minusDur).size());
        assertEquals(1, validator.validate(unnamed).size());

        final ValidationException exception = assertThrows(
                ValidationException.class,

                () -> filmController.addFilm(oldFilm));

        assertEquals("Дата релиза — не раньше 28 декабря 1895 года",
                exception.getMessage());

        filmController.updateFilm(changeFilm);
        Film example = filmController.getAllFilms().get(0);
        assertEquals(changeFilm, example, "Разные фильмы");
    }

    @Test
    void getAllFilms() throws ValidationException {
        int size = filmController.getAllFilms().size();
        assertEquals(0, size, "Not null size");

        filmController.addFilm(film1);
        int size1 = filmController.getAllFilms().size();
        assertEquals(1, size1, "Отличается размер мапы");
    }
}