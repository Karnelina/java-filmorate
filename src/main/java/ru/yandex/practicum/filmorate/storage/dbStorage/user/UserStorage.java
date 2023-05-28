package ru.yandex.practicum.filmorate.storage.dbStorage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    User create(User user);

    Optional<User> update(User user);

    Collection<User> getUsers();

    Optional<User> getUserById(long id);

    Collection<User> getUsersByIds(Collection<Long> ids);

    void deleteUser(long id);
}