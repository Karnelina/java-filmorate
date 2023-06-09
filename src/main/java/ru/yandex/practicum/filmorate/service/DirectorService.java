package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.dbStorage.director.DirectorStorage;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorStorage directorStorage;

    public Director getDirectorById(int directorId) {
        Director director = directorStorage.getDirectorById(directorId);
        log.info("Получен режиссёр id = " + director);
        return director;
    }

    public Collection<Director> getDirectors() {
        Collection<Director> directors = directorStorage.getAllDirectors();
        if (!CollectionUtils.isEmpty(directors)) {
            log.info("Получены режиссёры: " + directors);
            return directors;
        }
        log.info("Получен пустой список фильмов");
        return new ArrayList<>();
    }

    public Director updateDirector(Director director) {
        directorStorage.getDirectorById(director.getId());
        return directorStorage.updateDirector(director);
    }

    public Director createDirector(Director director) {
        directorStorage.createDirector(director);
        log.info("Режиссёр {} создан.", director.getName());
        return director;
    }

    public void deleteDirector(int id) {
        directorStorage.deleteDirector(id);
    }

}