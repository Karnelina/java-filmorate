package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.constant.UserConstant.*;

@Slf4j
@Component
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        log.debug("add({}).", user);
        Map<String, Object> keys = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName(USER_TABLE)
                .usingColumns(NAME, EMAIL, LOGIN, BIRTHDAY)
                .usingGeneratedKeyColumns(ID)
                .executeAndReturnKeyHolder(Map.of(LOGIN, user.getLogin(),
                        EMAIL, user.getEmail(),
                        NAME, user.getName(),
                        BIRTHDAY, java.sql.Date.valueOf(user.getBirthday())))
                .getKeys();
        assert keys != null;
        user.setId((Long) keys.get(ID));

        return user;
    }

    @Override
    public Optional<User> update(User user) {
        jdbcTemplate.update("UPDATE USERS SET USER_ID = ?, EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? "
                        + "where USER_ID = ?",
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return Optional.of(user);
    }

    @Override
    public Collection<User> getAllUsers() {
        String sqlToUserTable = "SELECT * FROM USERS";
        return jdbcTemplate.query(sqlToUserTable, (rs, rowNum) -> mapToUser(rs))
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> getUserById(long id) {
        String sqlToUserTable = "SELECT * FROM USERS WHERE USER_ID = ? ";
        return jdbcTemplate.query(sqlToUserTable, (rs, rowNum) -> mapToUser(rs), id)
                .stream()
                .filter(Objects::nonNull)
                .findFirst();
    }

    @Override
    public Collection<User> getUsersByIds(Set<Long> ids) {
        List<User> users = new ArrayList<>();
        if (CollectionUtils.isEmpty(ids)) {
            return users;
        }
        String inSql = ids.stream().map(Object::toString)
                .collect(Collectors.joining(", "));
        String sqlToUserTable = String.format("select * from USERS where USER_ID in ( %s )", inSql);
        return jdbcTemplate.query(sqlToUserTable, (rs, rowNum) -> mapToUser(rs))
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(long id) {
        String deleteSQL = "DELETE FROM USERS WHERE USER_ID = ?";
        this.jdbcTemplate.update(deleteSQL, id);
    }

    private User mapToUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getLong(ID))
                .email(rs.getString(EMAIL))
                .login(rs.getString(LOGIN))
                .name(rs.getString(NAME))
                .birthday(rs.getDate(BIRTHDAY).toLocalDate())
                .friendsId(new HashSet<>())
                .build();
    }

}