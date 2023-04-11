package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    UserController userController;
    User usualUser;
    User noEmailUser1;
    User noEmailUser2;
    User noLoginUser;
    User noNameUser;
    User wrongBirthdayUser;
    User updateUser;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();

        usualUser = new User(
                1,
                "email@email.ru",
                "Login",
                "Name",
                LocalDate.of(1999,3,29)
        );

        noEmailUser1 = new User(
                2,
                "",
                "Login",
                "Name",
                LocalDate.of(1999,3,29)
        );

        noEmailUser2 = new User(
                3,
                "emailemailru",
                "Login",
                "Name",
                LocalDate.of(1999,3,29)
        );

        noLoginUser = new User(
                4,
                "email@email.ru",
                "",
                "Name",
                LocalDate.of(1999,3,29)
        );

        noNameUser  = new User(
                5,
                "email@email.com",
                "Login",
                "",
                LocalDate.of(1999,3,29)
        );

        wrongBirthdayUser = new User(
                6,
                "email@email.ru",
                "Login",
                "Name",
                LocalDate.of(2025,3,29)
        );

        updateUser = new User(
                1,
                "email@email.ru",
                "newLogin",
                "newName",
                LocalDate.of(2005,6,10)
        );
    }

    @Test
    void getAllUsers() throws ValidationException {
        int size = userController.getAllUsers().size();
        assertEquals(0, size, "Not null size");

        userController.create(usualUser);
        int size1 = userController.getAllUsers().size();
        assertEquals(1, size1, "Отличается размер мапы");

    }

    @Test
    void create() throws ValidationException {
        int size = userController.getAllUsers().size();
        assertEquals(0, size, "Not null size");

        userController.create(usualUser);
        int size1 = userController.getAllUsers().size();
        assertEquals(1, size1, "Отличается размер мапы");

        final ValidationException exception = assertThrows(
                ValidationException.class,

                () -> userController.create(noEmailUser1));

        assertEquals("Введите корректный email",
                exception.getMessage());

        final ValidationException e = assertThrows(
                ValidationException.class,

                () -> userController.create(noEmailUser2));

        assertEquals("Введите корректный email",
                e.getMessage());

        final ValidationException ex = assertThrows(
                ValidationException.class,

                () -> userController.create(noLoginUser));

        assertEquals("Логин не может быть пустым и содержать пробелы",
                ex.getMessage());

        userController.create(noNameUser);
        String login = noNameUser.getLogin();
        String name = noNameUser.getName();
        assertEquals(login, name);

        final ValidationException ex2 = assertThrows(
                ValidationException.class,

                () -> userController.create(wrongBirthdayUser));

        assertEquals("Дата рождения не может быть в будущем",
                ex2.getMessage());
    }

    @Test
    void update() throws ValidationException {
        int size = userController.getAllUsers().size();
        assertEquals(0, size, "Not null size");

        userController.create(usualUser);
        int size1 = userController.getAllUsers().size();
        assertEquals(1, size1, "Отличается размер мапы");

        final ValidationException exception = assertThrows(
                ValidationException.class,

                () -> userController.update(noEmailUser1));

        assertEquals("Введите корректный email",
                exception.getMessage());

        final ValidationException e = assertThrows(
                ValidationException.class,

                () -> userController.update(noEmailUser2));

        assertEquals("Введите корректный email",
                e.getMessage());

        final ValidationException ex = assertThrows(
                ValidationException.class,

                () -> userController.update(noLoginUser));

        assertEquals("Логин не может быть пустым и содержать пробелы",
                ex.getMessage());

        userController.create(noNameUser);
        String login = noNameUser.getLogin();
        String name = noNameUser.getName();
        assertEquals(login, name);

        final ValidationException ex2 = assertThrows(
                ValidationException.class,

                () -> userController.update(wrongBirthdayUser));

        assertEquals("Дата рождения не может быть в будущем",
                ex2.getMessage());

        userController.update(updateUser);
        User example = userController.getAllUsers().get(0);
        assertEquals(updateUser, example);
    }
}