package ru.yandex.practicum.filmorate.storage.dbStorage.recommendations;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.impl.RecommendationsDao;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@Repository
@Primary
public class RecommendationDbStorage implements RecommendationStorage {

    private final RecommendationsDao recommendationsDao;

    public RecommendationDbStorage(RecommendationsDao recommendationsDao) {
        this.recommendationsDao = recommendationsDao;
    }

    @Override
    public Collection<Film> getUserRecommendations(long id) {
        return recommendationsDao.getUserRecommendations(id);
    }
}
