package ru.yandex.practicum.filmorate.storage.dbStorage.friendship;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.Collection;

public interface FriendshipStorage {
    Friendship createFriendship(Friendship friendship);

    void deleteFriendship(Friendship friendship);

    Collection<Long> getFriendIdsByUserId(long userId);
}