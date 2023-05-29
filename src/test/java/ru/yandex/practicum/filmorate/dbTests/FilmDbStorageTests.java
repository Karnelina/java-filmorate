package ru.yandex.practicum.filmorate.dbTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.dbStorage.film.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmDbStorageTests {

    private final FilmStorage filmStorage;

    Film film;

    @BeforeEach
    public void init() {
        film = Film.builder()
                .id(1L)
                .name("name")
                .description("description")
                .duration(1)
                .genres(new ArrayList<>())
                .likes(new ArrayList<>())
                .releaseDate(LocalDate.of(1997, 5, 5))
                .mpa(MpaRating.builder()
                        .id(1L)
                        .name("name")
                        .build())
                .build();
    }

    @Test
    public void createFilmTest() {
        Optional<Film> film1 = filmStorage.createFilm(film);

        assertThat(film1)
                .isPresent()
                .hasValueSatisfying(film2 -> assertThat(film2).hasFieldOrProperty("id"));
    }

    @Test
    public void getFilmsTest() {
        filmStorage.createFilm(film);
        Optional<Film> film1 = filmStorage.getAllFilms().stream().findFirst();

        assertThat(film1)
                .isPresent()
                .hasValueSatisfying(film2 -> assertThat(film2).hasFieldOrProperty("id"));
    }

    @Test
    public void updateFilmTest() {
        filmStorage.createFilm(film);

        Film updatedFilm = Film.builder()
                .id(1L)
                .name("updatedName")
                .description("description")
                .duration(1)
                .genres(new ArrayList<>())
                .likes(new ArrayList<>())
                .releaseDate(LocalDate.of(1997, 5, 5))
                .mpa(MpaRating.builder()
                        .id(1L)
                        .name("name")
                        .build())
                .build();

        filmStorage.updateFilm(updatedFilm);

        assertEquals("updatedName", filmStorage.getFilmById(1L).get().getName());
    }

    @Test
    public void getFilmByIdTest() {
        filmStorage.createFilm(film);
        assertEquals(film.getId(), filmStorage.getFilmById(film.getId()).get().getId());
    }
}
