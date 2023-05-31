package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.Collection;
import java.util.Optional;

public interface FriendshipDao {
    Friendship createFriendship(Friendship friendship);

    void deleteFriendship(Friendship friendship);

    Collection<Long> getFriendIdsByUserId(long userId);

    Optional<Friendship> getFriendship(Friendship friendship);
}
