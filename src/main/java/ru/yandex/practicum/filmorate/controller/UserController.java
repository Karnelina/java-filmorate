package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    int id = 0;

    private Integer countId() {
        return ++id;
    }


    @GetMapping("/users")
    public List<User> getAllUsers() {
        log.info("Получен запрос Get users.");
        return new ArrayList<>(users.values());
    }

    @PostMapping(value = "/users")
    @ResponseBody
    public User create(@Valid @RequestBody User user) throws ValidationException {
        log.info("Получен запрос Post user.");

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        User client = User.builder()
                .id(countId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();

        if (users.containsKey(client.getId())) {
            log.warn("Пользователь существует");

            throw new ValidationException("Пользователь существует");
        }

        users.put(client.getId(), client);
        log.info("Добавлен {} клиент", client);

        return client;
    }

    @PutMapping(value = "/users")
    @ResponseBody
    public User update(@Valid @RequestBody User user) throws ValidationException {
        log.info("Получен запрос Put user.");

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (users.containsKey(user.getId())) {
            users.get(user.getId()).setEmail(user.getEmail());
            users.get(user.getId()).setLogin(user.getLogin());
            users.get(user.getId()).setName(user.getName());
            users.get(user.getId()).setBirthday(user.getBirthday());

            log.info("Добавлен {} клиент", user);

            return user;

        } else {
            log.warn("Такого id не существует");

            throw new ValidationException("Такого id не существует");
        }
    }
}