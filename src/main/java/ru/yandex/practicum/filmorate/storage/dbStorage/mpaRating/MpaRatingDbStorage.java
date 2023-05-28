package ru.yandex.practicum.filmorate.storage.dbStorage.mpaRating;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaRatingDao;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MpaRatingDbStorage implements MpaRatingStorage {

    private final MpaRatingDao mpaRatingDao;

    @Override
    public Collection<MpaRating> getMpaRatings() {
        return mpaRatingDao.getMpaRatings();
    }

    @Override
    public Optional<MpaRating> getMpaRatingById(long mpaRatingId) {
        return mpaRatingDao.getMpaRatingById(mpaRatingId);
    }
}