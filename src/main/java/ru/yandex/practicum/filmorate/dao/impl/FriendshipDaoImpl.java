package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendshipDao;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.constant.FriendshipConstant.*;

@Slf4j
@Component
public class FriendshipDaoImpl implements FriendshipDao {
    private final JdbcTemplate jdbcTemplate;

    public FriendshipDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Friendship createFriendship(Friendship friendship) {
        log.debug("add({}).", friendship);
        boolean friendshipAlreadyExisted = friendshipStatusRevers(friendship, true);
        Map<String, Object> keys = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName(FRIENDSHIP_TABLE)
                .usingColumns(USER_ID, FRIEND_ID, STATUS)
                .usingGeneratedKeyColumns(ID)
                .executeAndReturnKeyHolder(Map.of(USER_ID, friendship.getUserId(),
                        FRIEND_ID, friendship.getFriendId(),
                        STATUS, friendshipAlreadyExisted))
                .getKeys();
        assert keys != null;
        friendship.setId((Long) keys.get(ID));
        return friendship;
    }

    @Override
    public Set<Long> getFriendIdsByUserId(long userId) {
        String sqlToFriendshipTable = "SELECT * FROM FRIENDS WHERE USER_ID = ?";
        return jdbcTemplate.query(sqlToFriendshipTable, (rs, rowNum) -> mapToFriendship(rs), userId)
                .stream()
                .filter(Objects::nonNull)
                .map(Friendship::getFriendId)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<Friendship> getFriendship(Friendship friendship) {
        String sqlToFriendshipTable = "SELECT * FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";

        return jdbcTemplate.query(sqlToFriendshipTable, (rs, rowNum) -> mapToFriendship(rs),
                        friendship.getUserId(),
                        friendship.getFriendId())
                .stream()
                .filter(Objects::nonNull)
                .findFirst();
    }

    @Override
    public void deleteFriendship(Friendship friendship) {
        friendshipStatusRevers(friendship, false);

        String sqlToFriendshipTable = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlToFriendshipTable, friendship.getUserId(),
                friendship.getFriendId());
    }

    private Friendship mapToFriendship(ResultSet userRows) throws SQLException {
        return new Friendship(
                userRows.getLong(ID),
                userRows.getLong(USER_ID),
                userRows.getLong(FRIEND_ID),
                userRows.getBoolean(STATUS)
        );
    }

    private boolean friendshipStatusRevers(Friendship friendship, boolean status) {
        Optional<Friendship> existedFriendshipOpt = getFriendship(friendship);
        if (existedFriendshipOpt.isPresent()) {
            Friendship existedFriendship = existedFriendshipOpt.get();
            existedFriendship.setStatus(status);
            updateFriendship(existedFriendship);
            return true;
        }
        return false;
    }

    private Friendship updateFriendship(Friendship friendship) {
        String sql = "UPDATE FRIENDS SET FRIENDS_ID = ?, USER_ID = ?, FRIEND_ID = ?, STATUS = ? " +
                " WHERE FRIENDS_ID = ? ";
        jdbcTemplate.update(sql,
                friendship.getId(),
                friendship.getUserId(),
                friendship.getFriendId(),
                friendship.getStatus(),
                friendship.getId());

        return friendship;
    }
}