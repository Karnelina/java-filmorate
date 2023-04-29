package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.time.DurationMin;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class Film {
    @PositiveOrZero
    Long id;
    @NotBlank
    String name;
    @Length(max = 200)
    String description;
    @PastOrPresent
    LocalDate releaseDate;
    @DurationMin(nanos = 1)
    Duration duration;
    Set<Long> popularFilms = new HashSet<>();

    public Film(Long id, String name, String description, LocalDate releaseDate, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public void setPopularFilms(Long userId) {
        this.popularFilms.add(userId);
    }
}