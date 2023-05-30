package ru.yandex.practicum.filmorate.dbTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dbStorage.user.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserDbStorageTests {

    private final UserStorage userStorage;

    private User user;

    @BeforeEach
    protected void init() {
        user = User.builder()
                .id(1L)
                .name("name")
                .login("login")
                .birthday(LocalDate.of(2002, 8, 8))
                .email("gfdsg@email.ru")
                .friendsId(new HashSet<>())
                .build();
    }

    @Test
    protected void testCreateUserTest() {
        Optional<User> user1 = Optional.of(userStorage.create(user));
        assertNotNull(user1, "Юзер не создан");
    }

    @Test
    protected void testUpdateUserTest() {
        userStorage.create(user);
        User updatedUser = User.builder()
                .id(1L)
                .name("updatedName")
                .login("login")
                .birthday(LocalDate.of(2002, 8, 8))
                .email("gfdsg@email.ru")
                .friendsId(new HashSet<>())
                .build();

        userStorage.update(updatedUser);
        assertEquals("updatedName", userStorage.getUserById(updatedUser.getId()).get().getName(),
                "Юзер не обновлен");
    }

    @Test
    protected void testGetUserByIdTest() {
        userStorage.create(user);
        assertEquals(user.getId(), userStorage.getUserById(user.getId()).get().getId(),
                "Получить юзера не удалось");
    }

    @Test
    protected void testGetUsersTest() {
        userStorage.create(user);
        Optional<User> user1 = userStorage.getUsers().stream().findFirst();

        assertThat(user1)
                .isPresent()
                .hasValueSatisfying(user2 -> assertThat(user2).hasFieldOrProperty("name"));
    }

    @Test
    protected void testGetUsersByIdsTest() {
        userStorage.create(user);
        Optional<User> user1 = userStorage.getUsersByIds(Set.of(1L)).stream().findFirst();

        assertThat(user1)
                .isPresent()
                .hasValueSatisfying(user2 -> assertThat(user2).hasFieldOrProperty("name"));
    }
}
