package ru.yandex.practicum.filmorate.dbTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.dbStorage.mpaRating.MpaRatingStorage;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MpaRatingDbStorageTests {

    private final MpaRatingStorage mpaRatingStorage;

    @Test
    public void testFindAllGpaRatings() {
        Optional<Collection<MpaRating>> mpaOptional = Optional.ofNullable(mpaRatingStorage.getMpaRatings());
        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa -> assertThat(mpa).first().hasFieldOrProperty("id"));
    }

    @Test
    public void testGetRatingById() {
        Optional<MpaRating> mpaOptional = mpaRatingStorage.getMpaRatingById(1);
        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa -> assertThat(mpa).hasFieldOrProperty("id"));
    }
}
