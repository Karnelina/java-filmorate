package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;

public interface DirectorDao {
    Director addDirector(Director director);

    Director updateDirector(Director director);

    Collection<Director> getAllDirectors();

    Director getDirectorById(int id);

    void deleteDirector(int id);

    Collection<Director> getDirectorsByFilmId(long id);
}