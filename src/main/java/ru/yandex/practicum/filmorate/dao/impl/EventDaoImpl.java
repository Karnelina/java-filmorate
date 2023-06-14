package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.EventDao;
import ru.yandex.practicum.filmorate.model.Event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

@Slf4j
@Component
public class EventDaoImpl implements EventDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EventDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addEvent(long userId, long entityId, String eventType, String eventOperation) {
        String sqlQuery = "INSERT INTO EVENTS (TIMESTAMP, USER_ID, EVENT_TYPE, EVENT_OPERATION, ENTITY_ID) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery, Instant.now().toEpochMilli(), userId, eventType, eventOperation, entityId);
    }

    @Override
    public List<Event> getEvents(long userId) {
        String sqlQuery = "SELECT TIMESTAMP, USER_ID, EVENT_TYPE, EVENT_OPERATION, ENTITY_ID, EVENT_ID " +
                "FROM EVENTS " +
                "WHERE USER_ID = ? " +
                "ORDER BY TIMESTAMP";
        List<Event> events = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapToEvent(rs), userId);
        return events;
    }

    private Event mapToEvent(ResultSet rs) throws SQLException {
        return Event.builder()
                .timestamp(rs.getLong("TIMESTAMP"))
                .userId(rs.getLong("USER_ID"))
                .eventType(rs.getString("EVENT_TYPE"))
                .operation(rs.getString("EVENT_OPERATION"))
                .eventId(rs.getLong("EVENT_ID"))
                .entityId(rs.getLong("ENTITY_ID"))
                .build();
    }
}
