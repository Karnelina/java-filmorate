package ru.yandex.practicum.filmorate.storage.dbStorage.user;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
@Primary
public class UserDbStorage implements UserStorage {
    private final UserDao userDao;

    public UserDbStorage(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User create(User user) {
        return userDao.create(user);
    }

    @Override
    public Optional<User> update(User user) {
        return userDao.update(user);
    }

    @Override
    public Collection<User> getUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public Collection<User> getUsersByIds(Collection<Long> ids) {
        return userDao.getUsersByIds((Set<Long>) ids);
    }

    @Override
    public void deleteUser(long id) {
        userDao.deleteUser(id);
    }

    @Override
    public Optional<User> getUserById(long userId) {
        return userDao.getUserById(userId);
    }

}