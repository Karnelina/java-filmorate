package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;

import java.util.Map;

import static ru.yandex.practicum.filmorate.constant.FilmGenreConstant.*;

@Slf4j
@Component
public class FilmGenreDaoImpl implements FilmGenreDao {
    private final JdbcTemplate jdbcTemplate;

    public FilmGenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void linkGenreToFilm(long filmId, long genreId) {
        if (linkAlreadyExist(filmId, genreId)) {
            return;
        }
        new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName(FILM_GENRE_TABLE)
                .usingColumns(FILM_ID, GENRE_ID)
                .usingGeneratedKeyColumns(ID)
                .executeAndReturnKeyHolder(Map.of(FILM_ID, filmId,
                        GENRE_ID, genreId))
                .getKeys();
    }

    @Override
    public void deleteFilmGenres(long filmId) {
        String sqlToGenreTable = "DELETE FROM GENRE_ID WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlToGenreTable, filmId);
    }

    public boolean linkAlreadyExist(long filmId, long genreId) {
        String sqlToGenreTable = "SELECT * FROM GENRE_ID WHERE FILM_ID = ? AND GENRE_ID = ? ";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlToGenreTable, filmId, genreId);
        return genreRows.next();
    }
}