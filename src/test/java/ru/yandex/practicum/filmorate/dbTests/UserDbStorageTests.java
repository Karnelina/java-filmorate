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
    public void init() {
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
    public void createUserTest() {
        Optional<User> user1 = Optional.of(userStorage.create(user));
        assertNotNull(user.getId());
    }

    @Test
    public void updateUserTest() {
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
        assertEquals("updatedName", userStorage.getUserById(updatedUser.getId()).get().getName());
    }

    @Test
    public void getUserByIdTest() {
        userStorage.create(user);
        assertEquals(user.getId(), userStorage.getUserById(user.getId()).get().getId());
    }

    @Test
    public void getUsersTest() {
        userStorage.create(user);
        Optional<User> user1 = userStorage.getUsers().stream().findFirst();

        assertThat(user1)
                .isPresent()
                .hasValueSatisfying(user2 -> assertThat(user2).hasFieldOrProperty("name"));
    }

    @Test
    public void getUsersByIdsTest() {
        userStorage.create(user);
        Optional<User> user1 = userStorage.getUsersByIds(Set.of(1L)).stream().findFirst();

        assertThat(user1)
                .isPresent()
                .hasValueSatisfying(user2 -> assertThat(user2).hasFieldOrProperty("name"));
    }
}
