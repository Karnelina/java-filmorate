package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();

        usualUser = new User(
                1,
                "email@email.ru",
                "Login",
                "Name",
                LocalDate.of(1999, 3, 29)
        );

        noEmailUser1 = new User(
                2,
                "",
                "Login",
                "Name",
                LocalDate.of(1999, 3, 29)
        );

        noEmailUser2 = new User(
                3,
                "emailemailru",
                "Login",
                "Name",
                LocalDate.of(1999, 3, 29)
        );

        noLoginUser = new User(
                4,
                "email@email.ru",
                "",
                "Name",
                LocalDate.of(1999, 3, 29)
        );

        noNameUser = new User(
                5,
                "email@email.com",
                "Login",
                "",
                LocalDate.of(1999, 3, 29)
        );

        wrongBirthdayUser = new User(
                6,
                "email@email.ru",
                "Login",
                "Name",
                LocalDate.of(2025, 3, 29)
        );

        updateUser = new User(
                1,
                "email@email.ru",
                "newLogin",
                "newName",
                LocalDate.of(2005, 6, 10)
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

        assertEquals(1, validator.validate(noEmailUser1).size());
        assertEquals(1, validator.validate(noEmailUser2).size());
        assertEquals(1, validator.validate(noLoginUser).size());
        assertEquals(1, validator.validate(wrongBirthdayUser).size());

        userController.create(noNameUser);
        String login = noNameUser.getLogin();
        String name = noNameUser.getName();
        assertEquals(login, name);
    }

    @Test
    void update() throws ValidationException {
        int size = userController.getAllUsers().size();
        assertEquals(0, size, "Not null size");

        userController.create(usualUser);
        int size1 = userController.getAllUsers().size();
        assertEquals(1, size1, "Отличается размер мапы");

        assertEquals(1, validator.validate(noEmailUser1).size());
        assertEquals(1, validator.validate(noEmailUser2).size());
        assertEquals(1, validator.validate(noLoginUser).size());
        assertEquals(1, validator.validate(wrongBirthdayUser).size());

        userController.create(noNameUser);
        String login = noNameUser.getLogin();
        String name = noNameUser.getName();
        assertEquals(login, name);

        userController.update(updateUser);
        User example = userController.getAllUsers().get(0);
        assertEquals(updateUser, example);
    }
}