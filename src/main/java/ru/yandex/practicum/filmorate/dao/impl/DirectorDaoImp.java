package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.constant.DirectorConstant.*;

@Slf4j
@Component
public class DirectorDaoImp implements DirectorDao {

    private final JdbcTemplate jdbcTemplate;

    public DirectorDaoImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Map<String, Object> directorToMap(Director director) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", director.getName());
        return values;
    }

    private RowMapper<Director> directorRowMapper() {
        return (rs, rowNum) -> new Director(rs.getInt(ID), rs.getString(NAME));
    }

    private int simpleSave(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(DIRECTOR_TABLE)
                .usingGeneratedKeyColumns(ID);
        return simpleJdbcInsert.executeAndReturnKey(directorToMap(director)).intValue();
    }

    private void delLink(int id) {
        delLinkFilmsDirector(id);
    }

    private void delLinkFilmsDirector(int id) {
        String sqlQuery = "DELETE FROM films_directors WHERE director_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Director addDirector(Director director) {
        String sqlQuery = "SELECT * FROM directors WHERE name = ?";
        Collection<Director> directors = jdbcTemplate.query(sqlQuery, directorRowMapper(), director.getName());
        if (directors.size() > 0) {
            throw new DirectorNotFoundException(String.format("Режиссер: %s, не добавлен так как он уже существует", director
                    .getName()));
        }
        director.setId(simpleSave(director));
        log.info("Добавлен режиссер: {}", director);
        return director;
    }

    @Override
    public Director updateDirector(Director director) {
        String sqlQuery = "UPDATE directors SET name = ? WHERE director_id = ?";
        jdbcTemplate.update(sqlQuery, director.getName(), director.getId());
        return director;
    }

    @Override
    public Collection<Director> getAllDirectors() {
        String sqlQuery = "SELECT * FROM directors ORDER BY director_id";
        return jdbcTemplate.query(sqlQuery, directorRowMapper());
    }

    @Override
    public Director getDirectorById(int id) {
        String sqlQuery = "SELECT * FROM directors WHERE director_id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, directorRowMapper(), id);
        } catch (DataAccessException e) {
            throw new DirectorNotFoundException(String.format("Режиссер с id = %d не найден", id));
        }
    }

    @Override
    public void deleteDirector(int id) {
        delLink(id);
        String deleteSQL = "DELETE FROM directors WHERE director_id = ?";
        this.jdbcTemplate.update(deleteSQL, id);
        log.info("Удален режиссер с id: {}", id);
    }

    @Override
    public Collection<Director> getDirectorsByFilmId(long filmId) {
        String sqlQuery = "SELECT d.director_id, d.name " +
                "FROM directors AS d " +
                "JOIN films_directors AS fd ON fd.director_id = d.director_id " +
                "WHERE fd.film_id = ? ";
        return jdbcTemplate.query(sqlQuery, directorRowMapper(), filmId)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}