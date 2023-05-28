package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.model.FilmLike;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.constant.FilmLikeConstant.*;

@Slf4j
@Component
public class FilmLikeDaoImpl implements FilmLikeDao {
    private final JdbcTemplate jdbcTemplate;

    public FilmLikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long createLike(FilmLike filmLike) {
        log.debug("add({}).", filmLike);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(FILM_LIKE_TABLE)
                .usingGeneratedKeyColumns(LIKE_ID);

        return simpleJdbcInsert.executeAndReturnKey(filmLike.toMap()).longValue();
    }

    @Override
    public List<FilmLike> getFilmLikes(long filmId) {
        String sqlToFilmLikeTable = "SELECT * FROM FILM_LIKE WHERE FILM_ID = ? ";
        return jdbcTemplate.query(sqlToFilmLikeTable, (rs, rowNum) -> mapToFilmLike(rs), filmId)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void unlike(FilmLike filmLike) {
        String sqlToFilmLikeTable = "DELETE FROM FILM_LIKE WHERE USER_ID = ? AND FILM_ID = ?";
        jdbcTemplate.update(sqlToFilmLikeTable, filmLike.getUserId(),
                filmLike.getFilmId());
    }

    private FilmLike mapToFilmLike(ResultSet filmLikeRows) throws SQLException {
        return FilmLike.builder()
                .userId(filmLikeRows.getLong(USER_ID))
                .filmId(filmLikeRows.getLong(FILM_ID))
                .build();
    }
}