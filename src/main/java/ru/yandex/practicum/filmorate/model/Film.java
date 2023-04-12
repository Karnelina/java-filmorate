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

@Data
@Builder
public class Film {
    @PositiveOrZero
    int id;
    @NotBlank
    String name;
    @Length(max = 200)
    String description;
    @PastOrPresent
    LocalDate releaseDate;
    @DurationMin(nanos = 1)
    Duration duration = Duration.ZERO;

    public Film(int id, String name, String description, LocalDate releaseDate, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}