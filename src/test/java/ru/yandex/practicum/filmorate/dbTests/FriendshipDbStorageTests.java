package ru.yandex.practicum.filmorate.dbTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dbStorage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.dbStorage.user.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FriendshipDbStorageTests {

    private final FriendshipStorage friendshipStorage;

    private final UserStorage userStorage;

    User user;

    User friend;

    Friendship friendship;

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

        friend = User.builder()
                .id(2L)
                .name("name")
                .login("login")
                .birthday(LocalDate.of(2002, 8, 8))
                .email("gfdsg@email.ru")
                .friendsId(new HashSet<>())
                .build();

        userStorage.create(user);
        userStorage.create(friend);

        friendship = Friendship.builder()
                .id(1L)
                .userId(1L)
                .friendId(2L)
                .status(false)
                .build();
    }

    @Test
    public void createFriendshipTest() {
        Optional<Friendship> friendship1 = Optional.ofNullable(friendshipStorage.createFriendship(friendship));
        assertThat(friendship1)
                .isPresent()
                .hasValueSatisfying(friendship2 -> assertThat(friendship2).hasFieldOrProperty("id"));
    }

    @Test
    public void deleteFriendshipTest() {
        Optional<Friendship> friendship1 = Optional.ofNullable(friendshipStorage.createFriendship(friendship));
        assertThat(friendship1)
                .isPresent()
                .hasValueSatisfying(friendship2 -> assertThat(friendship2).hasFieldOrProperty("id"));

        assertEquals(1, friendshipStorage.getFriendIdsByUserId(1).size());
        friendshipStorage.deleteFriendship(friendship);
        assertEquals(0, friendshipStorage.getFriendIdsByUserId(1).size());
    }

    @Test
    public void getFriendIdsByUserIdTest() {
        assertEquals(0, friendshipStorage.getFriendIdsByUserId(1).size());

        Optional<Friendship> friendship1 = Optional.ofNullable(friendshipStorage.createFriendship(friendship));
        assertThat(friendship1)
                .isPresent()
                .hasValueSatisfying(friendship2 -> assertThat(friendship2).hasFieldOrProperty("id"));

        assertEquals(1, friendshipStorage.getFriendIdsByUserId(1).size());
    }
}
