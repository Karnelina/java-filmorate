package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        log.info("Получен запрос Get users.");
        return userService.getAllUsers();
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) throws ValidationException {
        log.info("Получен запрос Post user.");

        return userService.create(user);
    }

    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) throws ValidationException {
        log.info("Получен запрос Put user.");

        return userService.update(user);
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") long id) throws ValidationException {
        return userService.getUser(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") long userId,
                          @PathVariable("friendId") long friendId) throws ValidationException {
        log.info("Получен запрос Put friend.");

        userService.addFriend(userId, friendId);

        log.info("Друг добавлен" + friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") long userId,
                             @PathVariable("friendId") long friendId) throws ValidationException {
        log.info("Получен запрос Delete friend.");

        userService.deleteFriend(userId, friendId);

        log.info("Друг удален.");
    }

    @GetMapping("/users/{id}/friends")
    public Collection<User> getUserFriends(@PathVariable("id") long id) throws ValidationException {
        return userService.getUserFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable("id") long userId, @PathVariable("otherId") long otherId) throws ValidationException {
        log.info("Получен запрос Get common friends.");
        return userService.getCommonFriends(userId, otherId);
    }

    @DeleteMapping("/users/{id}/delete")
    public void deleteFilm(@PathVariable("id") long id) {
        userService.deleteUser(id);
    }
}