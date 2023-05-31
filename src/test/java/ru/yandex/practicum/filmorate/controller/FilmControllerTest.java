package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmControllerTest {
    private FilmController filmController;
    private Film film;
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @BeforeEach
    protected void setUp() {
        film = Film.builder()
                .id(1L)
                .name("name")
                .description("description")
                .duration(65)
                .releaseDate(LocalDate.of(2002, 2, 2))
                .likes(new ArrayList<>())
                .genres(new ArrayList<>())
                .mpa(MpaRating.builder()
                        .id(1)
                        .name("name")
                        .build())
                .build();
    }

    @Test
    protected void nullRequestTest() {
        film = null;
        assertThrows(NullPointerException.class, () -> filmController.createFilm(film), "Фильм существует");
    }

    @Test
    protected void idMissedTest() {
        assertThrows(NullPointerException.class, () -> filmController.updateFilm(film), "Фильм существует");
    }

    @Test
    protected void blankNameTest() {
        film.setName(null);
        assertEquals(1, validator.validate(film).size());
    }

    @Test
    protected void nullNameTest() {
        film.setName("");
        assertEquals(1, validator.validate(film).size());

        film.setName(" ");
        assertEquals(1, validator.validate(film).size());
    }

    @Test
    protected void moreThanTwoHundredCharactersDescriptionTest() {
        film.setDescription("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        assertEquals(1, validator.validate(film).size());
    }

    @Test
    protected void releaseDateIsBefore28_1895Test() {
        film.setReleaseDate(LocalDate.parse("28-12-1894", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        assertThrows(NullPointerException.class, () -> filmController.updateFilm(film),
                "Фильм создан после 28.12.1895");
    }

    @Test
    protected void negativeDurationTest() {
        film.setDuration(-200);
        assertEquals(1, validator.validate(film).size(), "Длительность положительная");
    }

    @Test
    protected void negativeIdTest() {
        film.setId(-1L);
        assertEquals(1, validator.validate(film).size(), "Айди положительное");
    }
}
