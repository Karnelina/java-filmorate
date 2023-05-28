package ru.yandex.practicum.filmorate.storage.inMemoryStorage.user;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface InMemUserStorage {
    User create(User user);

    User update(User user) throws ValidationException;

    User getUser(long id) throws ValidationException;

    List<User> getAllUsers();

    Collection<User> getUsersByIds(Collection<Long> ids) throws ValidationException;

    void deleteUser(long id);
}