package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.constant.GenreConstant;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenres() {
        String sqlToGenreTable = "SELECT * FROM GENRE";
        return jdbcTemplate.query(sqlToGenreTable, (rs, rowNum) -> mapToGenre(rs))
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Genre> getGenreById(long id) {
        String sqlToGenreTable = "SELECT * FROM GENRE WHERE GENRE_ID = ? ";
        return jdbcTemplate.query(sqlToGenreTable, (rs, rowNum) -> mapToGenre(rs), id)
                .stream()
                .filter(Objects::nonNull)
                .findFirst();
    }

    @Override
    public List<Genre> getGenresByFilmId(long filmId) {
        String sqlToGenreTable = "SELECT g.GENRE_ID, g.NAME FROM GENRE AS g " +
                "JOIN GENRE_ID AS gi ON gi.GENRE_ID = g.GENRE_ID " +
                "WHERE gi.FILM_ID = ? ";
        return jdbcTemplate.query(sqlToGenreTable, (rs, rowNum) -> mapToGenre(rs), filmId)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Genre mapToGenre(ResultSet rs) throws SQLException {
        return Genre.builder()
                .id(rs.getLong(GenreConstant.ID))
                .name(rs.getString(GenreConstant.NAME))
                .build();
    }
}
