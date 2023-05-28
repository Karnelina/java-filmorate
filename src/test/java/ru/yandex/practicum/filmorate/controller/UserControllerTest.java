package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
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
class UserControllerTest {

    private UserController userController;
    private User user;
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @BeforeEach
    public void setUp() {
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
    public void idMissedTest() {
        assertThrows(NullPointerException.class, () -> userController.updateUser(user));
    }

    @Test
    public void emailWithoutAtTest() {
        user.setEmail("mail.ru");
        assertEquals(1, validator.validate(user).size());
    }

    @Test
    public void blankEmailTest() {
        user.setEmail(" ");
        assertEquals(2, validator.validate(user).size());
    }

    @Test
    public void nullEmailTest() {
        user.setEmail(null);
        assertEquals(1, validator.validate(user).size());
    }

    @Test
    public void blankLoginTest() {
        user.setLogin("");
        assertEquals(1, validator.validate(user).size());

        user.setLogin(" ");
        assertEquals(1, validator.validate(user).size());
    }

    @Test
    public void nullLoginTest() {
        user.setLogin(null);
        assertEquals(1, validator.validate(user).size());
    }

    @Test
    public void nullNameTest() {
        user.setName(null);
        assertEquals(user.getLogin(), "login");
    }

    @Test
    public void blankNameTest() {
        user.setName("");
        assertEquals(user.getLogin(), "login");

        user.setName(" ");
        assertEquals(user.getLogin(), "login");
    }

    @Test
    public void birthdayInFutureTest() {
        user.setBirthday(LocalDate.parse("20-08-2446", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        assertEquals(1, validator.validate(user).size());
    }

    @Test
    public void nullRequestTest() {
        assertThrows(NullPointerException.class, () -> userController.createUser(null));
    }

    @Test
    public void negativeIdTest() {
        user.setId(-1L);
        assertEquals(1, validator.validate(user).size());
    }
}