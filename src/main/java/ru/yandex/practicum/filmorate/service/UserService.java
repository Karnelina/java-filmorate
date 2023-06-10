package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dbStorage.event.EventStorage;
import ru.yandex.practicum.filmorate.storage.dbStorage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final EventStorage eventStorage;

    public User getUserById(long userId) {
        if (!isExist(userId)) {
            log.error("Ошибка, пользователь не существует: " + userId);
            throw new UserNotFoundException("Пользователь не существует");
        }

        User user = userStorage.getUserById(userId).orElseThrow(UserNotFoundException::new);
        log.info("Получен пользователь: " + user);
        return user;
    }

    public User createUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        User newUser = userStorage.create(user);
        log.info("Создан пользователь: " + newUser);
        return user;
    }

    public User updateUser(User user) {
        if (!isExist(user.getId())) {
            log.error("Ошибка, пользователь не существует: " + user);
            throw new UserNotFoundException("Пользователь не существует");
        }
        userStorage.update(user);
        log.info("Обновлен пользователь: " + user);
        return user;
    }

    public Collection<User> getUsers() {
        Collection<User> users = userStorage.getUsers();
        log.info("Получены пользователи: " + users);
        return users;
    }

    private boolean isExist(long userId) {
        return userStorage.getUserById(userId).isPresent();
    }

    public Collection<User> getUsersByIds(Collection<Long> ids) {
        return userStorage.getUsersByIds(ids);
    }

    public void deleteUser(long id) {
        userStorage.deleteUser(id);
    }

    public List<Event> getEvents(long id){
        return eventStorage.getEvents(id);
    }
}