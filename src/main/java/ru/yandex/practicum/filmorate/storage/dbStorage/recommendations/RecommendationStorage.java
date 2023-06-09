package ru.yandex.practicum.filmorate.storage.dbStorage.recommendations;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface RecommendationStorage {

    Collection<Film> getUserRecommendations(long id);
}
