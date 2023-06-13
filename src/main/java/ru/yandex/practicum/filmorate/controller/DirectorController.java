package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/directors")
public class DirectorController {

    @Autowired
    private final DirectorService directorService;

    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @PostMapping
    public Director createDirector(@Valid @RequestBody Director director) {
        log.info("Добавление режиссера: {}", director.getName());
        directorService.createDirector(director);
        return director;
    }

    @DeleteMapping("/{id}")
    public void deleteDirector(@PathVariable(value = "id") Integer idDirector) {
        log.info("Удаление режиссера с id: {}", idDirector);
        directorService.deleteDirector(idDirector);
    }

    @PutMapping
    public Director updateDirector(@Valid @RequestBody Director director) {
        log.info("Изменение режиссера: {}", director.getName());
        directorService.updateDirector(director);
        return director;
    }

    @GetMapping("/{id}")
    public Director getDirectorById(@PathVariable(value = "id") Integer idDirector) {
        log.info("Вывод режиссера по id: {}", idDirector);
        return directorService.getDirectorById(idDirector);
    }

    @GetMapping
    public Collection<Director> getDirectors() {
        log.info("Вывод всех режиссеров...");
        return directorService.getDirectors();
    }
}