package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {
    FilmController filmController;
    Film film1;
    Film oldFilm;
    Film unnamed;
    Film overDescription;
    Film minusDur;
    Film updateFilm;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();

        film1 = new Film(
                1,
                "Film",
                "Interesting Film",
                LocalDate.of(2022, 12, 20),
                Duration.ofMinutes(120)
        );

        oldFilm = new Film(
                2,
                "Old Film",
                "Old Film",
                LocalDate.of(1895, 12, 24),
                Duration.ofMinutes(60)
        );

        unnamed = new Film(
                3,
                "",
                "Interesting Film",
                LocalDate.of(2022, 12, 20),
                Duration.ofMinutes(120)
        );

        overDescription = new Film(
                4,
                "Film",
                "Interesting Film jffokjfojjnjfgohgoidfosiegoiegosidjgosiegoisegofieoighosiegefglkjdlfkjd " +
                        "doifjoifgoisjdoiajofoasijdpioasjopfijoihfgohoaijsdf[j{Dfj[DIjfoigjh[oidjgoijdogjdfkdokfjdfdf" +
                        "fhjoksdjokdjfoksjdfoijdflksdjfosidjfosijdfosjdfosidjfosidjfosidfjosidjfsodifjsodfosidjfosijdf",
                LocalDate.of(2022, 12, 20),
                Duration.ofMinutes(120)
        );

        minusDur = new Film(
                5,
                "Film",
                "Interesting Film",
                LocalDate.of(2022, 12, 20),
                Duration.ofMinutes(-120)
        );

        updateFilm = new Film(
                1,
                "newFilm",
                "New interesting Film",
                LocalDate.of(2020, 10, 10),
                Duration.ofMinutes(30)
        );
    }


    @Test
    public void addFilm() throws ValidationException {
        int size = filmController.getAllFilms().size();
        assertEquals(0, size, "Not null size");

        filmController.addFilm(film1);
        int size1 = filmController.getAllFilms().size();
        assertEquals(1, size1, "Отличается размер мапы");

        final ValidationException exception = assertThrows(
                ValidationException.class,

                () -> filmController.addFilm(unnamed));

        assertEquals("Название не может быть пустым",
                exception.getMessage());

        final ValidationException e = assertThrows(
                ValidationException.class,

                () -> filmController.addFilm(overDescription));

        assertEquals("Максимальная длина описания — 200 символов",
                e.getMessage());

        final ValidationException ex = assertThrows(
                ValidationException.class,

                () -> filmController.addFilm(minusDur));

        assertEquals("Продолжительность фильма должна быть положительной",
                ex.getMessage());

    }

    @Test
    void updateFilm() throws ValidationException {
        int size = filmController.getAllFilms().size();
        assertEquals(0, size, "Not null size");

        filmController.addFilm(film1);
        int size1 = filmController.getAllFilms().size();
        assertEquals(1, size1, "Отличается размер мапы");

        final ValidationException exception = assertThrows(
                ValidationException.class,

                () -> filmController.updateFilm(unnamed));

        assertEquals("Название не может быть пустым",
                exception.getMessage());

        final ValidationException e = assertThrows(
                ValidationException.class,

                () -> filmController.updateFilm(overDescription));

        assertEquals("Максимальная длина описания — 200 символов",
                e.getMessage());

        final ValidationException ex = assertThrows(
                ValidationException.class,

                () -> filmController.updateFilm(minusDur));

        assertEquals("Продолжительность фильма должна быть положительной",
                ex.getMessage());

        filmController.updateFilm(updateFilm);
        Film example = filmController.getAllFilms().get(0);
        assertEquals(updateFilm, example, "Разные фильмы");
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
