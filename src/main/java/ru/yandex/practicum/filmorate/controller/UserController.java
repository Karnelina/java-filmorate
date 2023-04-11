package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
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
    public User create(@RequestBody User user) throws ValidationException {
        log.info("Получен запрос Post user.");

        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Введите корректный email");
        }

        if (user.getLogin().isBlank() || user.getLogin() == null) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        User client = User.builder()
                .id(countId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();

        if (users.containsKey(client.getId())) {
            throw new ValidationException("Пользователь существует");
        }

        users.put(client.getId(), client);

        return client;
    }

    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) throws ValidationException {
        log.info("Получен запрос Put user.");

        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Введите корректный email");
        }

        if (user.getLogin().isBlank() || user.getLogin() == null) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        if (users.containsKey(user.getId())) {
            users.get(user.getId()).setEmail(user.getEmail());
            users.get(user.getId()).setLogin(user.getLogin());
            users.get(user.getId()).setName(user.getName());
            users.get(user.getId()).setBirthday(user.getBirthday());

            return user;

        } else {
            throw new ValidationException("Такого id не существует");
        }
    }
}