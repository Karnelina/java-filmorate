package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.constant.MpaRatingConstant;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.constant.MpaRatingConstant.ID;

@Slf4j
@Component
public class MpaRatingDaoImpl implements MpaRatingDao {
    private final JdbcTemplate jdbcTemplate;

    public MpaRatingDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MpaRating> getMpaRatings() {
        String sqlToMpaRatingTable = "select * from MPA_RATING";
        return jdbcTemplate.query(sqlToMpaRatingTable, (rs, rowNum) -> mapToMpaRating(rs))
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MpaRating> getMpaRatingById(long id) {
        String sqlToMpaRatingTable = "select * from MPA_RATING where RATING_ID = ? ";
        return jdbcTemplate.query(sqlToMpaRatingTable, (rs, rowNum) -> mapToMpaRating(rs), id)
                .stream()
                .filter(Objects::nonNull)
                .findFirst();
    }

    private MpaRating mapToMpaRating(ResultSet rs) throws SQLException {
        return new MpaRating(
                rs.getLong(ID),
                rs.getString(MpaRatingConstant.NAME));
    }
}
