package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MpaRatingNotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.dbStorage.mpaRating.MpaRatingDbStorage;

import java.util.Collection;

@Service
@Slf4j
public class MpaRatingService {
    private final MpaRatingDbStorage mpaRatingStorage;

    public MpaRatingService(MpaRatingDbStorage mpaRatingStorage) {
        this.mpaRatingStorage = mpaRatingStorage;
    }

    public MpaRating getMpaRatingById(long mpaRatingId) {
        var mpaRating = mpaRatingStorage.getMpaRatingById(mpaRatingId);
        if (mpaRating.isPresent()) {
            log.info("Получен MPA рейтинг: " + mpaRating);

            return mpaRating.get();
        }
        throw new MpaRatingNotFoundException("Рейтинг с таким id не существует");
    }

    public Collection<MpaRating> getMpaRatings() {
        Collection<MpaRating> ratings = mpaRatingStorage.getMpaRatings();
        log.info("Получены MPA рейтинги: " + ratings);
        return ratings;
    }
}