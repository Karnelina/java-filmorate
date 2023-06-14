package ru.yandex.practicum.filmorate.dao.impl;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface RecommendationsDao {

    Collection<Film> getUserRecommendations(long id);

}
