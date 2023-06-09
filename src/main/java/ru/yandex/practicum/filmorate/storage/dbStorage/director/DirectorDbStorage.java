package ru.yandex.practicum.filmorate.storage.dbStorage.director;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.model.Director;
import java.util.Collection;

@Repository
@Primary
@Slf4j
public class DirectorDbStorage implements DirectorStorage {

    private final DirectorDao directorDao;

    public DirectorDbStorage(DirectorDao directorDao) {
        this.directorDao = directorDao;
    }

    @Override
    public Collection<Director> getAllDirectors() {
        return directorDao.getAllDirectors();
    }

    @Override
    public Director updateDirector(Director director) {
        director = directorDao.updateDirector(director);
        log.info("Обновлен режиссёр: " + director);
        return director;
    }

    @Override
    public Director createDirector(Director director) {
        return directorDao.addDirector(director);
    }

    @Override
    public Director getDirectorById(int id) {
        return directorDao.getDirectorById(id);
    }

    @Override
    public void deleteDirector(int id) {
        directorDao.deleteDirector(id);
    }

    @Override
    public Collection<Director> getDirectorsByFilmId(long filmId) {
        Collection<Director> directors = directorDao.getDirectorsByFilmId(filmId);
        log.info("Получены режиссёры: " + directors);
        return directors;
    }
}