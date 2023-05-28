package ru.yandex.practicum.filmorate.storage.dbStorage.friendship;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FriendshipDao;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.Collection;

@Repository
@Slf4j
public class FriendshipDbStorage implements FriendshipStorage {
    private final FriendshipDao friendshipDao;

    public FriendshipDbStorage(FriendshipDao friendshipDao) {
        this.friendshipDao = friendshipDao;
    }

    @Override
    public Friendship createFriendship(Friendship friendship) {
        friendshipDao.createFriendship(friendship);
        log.info("Создана дружба: " + friendship);
        return friendship;
    }

    @Override
    public void deleteFriendship(Friendship friendship) {
        friendshipDao.deleteFriendship(friendship);
        log.info("Удалена дружба: " + friendship);
    }

    @Override
    public Collection<Long> getFriendIdsByUserId(long userId) {
        Collection<Long> friendsId = friendshipDao.getFriendIdsByUserId(userId);
        log.info("Получены друзья: " + friendsId);
        return friendsId;
    }
}