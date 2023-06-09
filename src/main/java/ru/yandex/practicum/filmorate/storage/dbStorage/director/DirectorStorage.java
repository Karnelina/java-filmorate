package ru.yandex.practicum.filmorate.storage.dbStorage.director;

import ru.yandex.practicum.filmorate.model.Director;
import java.util.Collection;

public interface DirectorStorage {

    Collection<Director> getAllDirectors();

    Director updateDirector(Director director);

    Director createDirector(Director director);

    Director getDirectorById(int id);

    void deleteDirector(int id);

    Collection<Director> getDirectorsByFilmId(long filmId);
}