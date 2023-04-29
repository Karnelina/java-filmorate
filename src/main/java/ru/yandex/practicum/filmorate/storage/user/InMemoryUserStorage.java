package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    long id = 0L;

    private Long countId() {
        return ++id;
    }

    @Override
    public User create(User user) {

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(countId());
        users.put(user.getId(), user);

        log.info("Добавлен {} клиент", user);

        return user;
    }

    @Override
    public User update(User user) {

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);

            log.info("Обновлен {} пользователь", user);

            return user;

        } else {

            throw new UserNotFoundException("Такого id не существует");
        }
    }

    @Override
    public User getUser(long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }
        throw new UserNotFoundException("Такого id не существует");
    }

    @Override
    public List<User> getAllUsers() {

        return new ArrayList<>(users.values());
    }

    public Collection<User> getUsersByIds(Collection<Long> ids) {
        List<User> result = new ArrayList<>();
        for (long id : ids) {
            if (!users.containsKey(id)) {
                throw new UserNotFoundException("Такого id не существует");
            }
            result.add(users.get(id));
        }
        return result;
    }

}