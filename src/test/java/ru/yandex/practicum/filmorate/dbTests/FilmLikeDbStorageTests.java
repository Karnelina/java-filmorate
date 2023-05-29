package ru.yandex.practicum.filmorate.dbTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLike;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dbStorage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dbStorage.filmLike.FilmLikeStorage;
import ru.yandex.practicum.filmorate.storage.dbStorage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmLikeDbStorageTests {

    private final FilmLikeStorage filmLikeStorage;

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    private FilmLike filmLike;

    private Film film;

    private User user;

    @BeforeEach
    public void init() {
        filmLike = FilmLike.builder()
                .filmId(1)
                .userId(1)
                .build();

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
                        .name("G")
                        .build())
                .build();

        user = User.builder()
                .id(1L)
                .name("name")
                .login("login")
                .birthday(LocalDate.of(2002, 8, 8))
                .email("gfdsg@email.ru")
                .friendsId(new HashSet<>())
                .build();

        filmStorage.createFilm(film);
        userStorage.create(user);
    }

    @Test
    public void createLikeTest() {
        Optional<FilmLike> filmLike1 = Optional.ofNullable(filmLikeStorage.createLike(filmLike));
        assertThat(filmLike1)
                .isPresent()
                .hasValueSatisfying(filmLike2 -> assertThat(filmLike2).hasFieldOrProperty("id"));
    }

    @Test
    public void getFilmLikesTest() {
        filmLikeStorage.createLike(filmLike);
        Optional<FilmLike> like = filmLikeStorage.getFilmLikes(1).stream().findFirst();

        assertThat(like)
                .isPresent()
                .hasValueSatisfying(like1 -> assertThat(like1).hasFieldOrProperty("userId"));
    }

    @Test
    public void unlikeTest() {
        filmLikeStorage.createLike(filmLike);
        Optional<FilmLike> like = filmLikeStorage.getFilmLikes(1).stream().findFirst();

        assertThat(like)
                .isPresent()
                .hasValueSatisfying(like1 -> assertThat(like1).hasFieldOrProperty("userId"));

        assertEquals(1, filmLikeStorage.getFilmLikes(1).size());

        filmLikeStorage.unlike(filmLike);

        assertEquals(0, filmLikeStorage.getFilmLikes(1).size());
    }
}
