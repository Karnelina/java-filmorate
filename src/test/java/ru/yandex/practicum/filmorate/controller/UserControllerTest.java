package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {

    private UserController userController;
    private User user;
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @BeforeEach
    protected void setUp() {
        user = User.builder()
                .id(1L)
                .name("name")
                .login("login")
                .email("email@fsdf.ru")
                .birthday(LocalDate.of(2002, 11, 11))
                .friendsId(new HashSet<>())
                .build();
    }

    @Test
    protected void idMissedTest() {
        assertThrows(NullPointerException.class, () -> userController.updateUser(user));
    }

    @Test
    protected void emailWithoutAtTest() {
        user.setEmail("mail.ru");
        assertEquals(1, validator.validate(user).size(), "Правильный емаил");
    }

    @Test
    protected void blankEmailTest() {
        user.setEmail(" ");
        assertEquals(2, validator.validate(user).size(), "Правильный емаил");
    }

    @Test
    protected void nullEmailTest() {
        user.setEmail(null);
        assertEquals(1, validator.validate(user).size(), "Правильный емаил");
    }

    @Test
    protected void blankLoginTest() {
        user.setLogin("");
        assertEquals(1, validator.validate(user).size(), "Логин не пустой");

        user.setLogin(" ");
        assertEquals(1, validator.validate(user).size(), "Логин не пустой");
    }

    @Test
    protected void nullLoginTest() {
        user.setLogin(null);
        assertEquals(1, validator.validate(user).size(), "Логин не пустой");
    }

    @Test
    protected void nullNameTest() {
        user.setName(null);
        assertEquals(user.getLogin(), "login", "Имя другое");
    }

    @Test
    protected void blankNameTest() {
        user.setName("");
        assertEquals(user.getLogin(), "login", "Имя другое");

        user.setName(" ");
        assertEquals(user.getLogin(), "login", "Имя другое");
    }

    @Test
    protected void birthdayInFutureTest() {
        user.setBirthday(LocalDate.parse("20-08-2446", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        assertEquals(1, validator.validate(user).size(), "День рождение в норме");
    }

    @Test
    protected void nullRequestTest() {
        assertThrows(NullPointerException.class, () -> userController.createUser(null));
    }

    @Test
    protected void negativeIdTest() {
        user.setId(-1L);
        assertEquals(1, validator.validate(user).size());
    }
}